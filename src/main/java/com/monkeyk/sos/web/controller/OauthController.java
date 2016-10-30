package com.monkeyk.sos.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.security.Principal;
import java.util.*;

/**
 * Created by poppet on 16/10/29.
 */
@Controller
@SessionAttributes("authorizationRequest")
public class OauthController {

    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

    private RedirectResolver redirectResolver = new DefaultRedirectResolver();

    private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

    @Resource
    UserApprovalHandler oauthUserApprovalHandler;

    @Resource
    OAuth2RequestFactory oAuth2RequestFactory;

    @Resource
    ClientDetailsService clientDetailsService;

    private WebResponseExceptionTranslator providerExceptionHandler = new DefaultWebResponseExceptionTranslator();

    private final String userApprovalPage = "forward:/oauth/confirm_access";

    private final String errorPage = "forward:/oauth/error";

    private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

    @Resource
    AuthorizationCodeServices authorizationCodeService;

    @RequestMapping(value = "/oauth/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> map,
                                  SessionStatus sessionStatus, Principal principal) {

        // Pull out the authorization request first, using the OAuth2RequestFactory. All further logic should
        // query off of the authorization request instead of referring back to the parameters map. The contents of the
        // parameters map will be stored without change in the AuthorizationRequest object once it is created.

        //Convert coming parameters to what spring-security-oauth need
        Map<String, String> parameters = new HashMap<>();

        parameters.put("client_id", map.get("appNum"));
        parameters.put("redirect_uri", map.get("redirectUrl"));
        parameters.put("state", map.get("state"));
        parameters.put("response_type", map.get("responseType"));
        parameters.put("scope", map.get("scope"));


        AuthorizationRequest authorizationRequest = oAuth2RequestFactory.createAuthorizationRequest(parameters);

        Set<String> responseTypes = authorizationRequest.getResponseTypes();

        if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
            throw new UnsupportedResponseTypeException("Unsupported response types: " + responseTypes);
        }

        if (authorizationRequest.getClientId() == null) {
            throw new InvalidClientException("A client id must be provided");
        }

        try {

            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                throw new InsufficientAuthenticationException(
                        "User must be authenticated with Spring Security before authorization can be completed.");
            }

            ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());

            // The resolved redirect URI is either the redirect_uri from the parameters or the one from
            // clientDetails. Either way we need to store it on the AuthorizationRequest.
            String redirectUriParameter = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
            String resolvedRedirect = redirectResolver.resolveRedirect(redirectUriParameter, client);
            if (!StringUtils.hasText(resolvedRedirect)) {
                throw new RedirectMismatchException(
                        "A redirectUri must be either supplied or preconfigured in the ClientDetails");
            }
            authorizationRequest.setRedirectUri(resolvedRedirect);

            // We intentionally only validate the parameters requested by the client (ignoring any data that may have
            // been added to the request by the manager).
            oauth2RequestValidator.validateScope(authorizationRequest, client);

            // Some systems may allow for approval decisions to be remembered or approved by default. Check for
            // such logic here, and set the approved flag on the authorization request accordingly.
            authorizationRequest = oauthUserApprovalHandler.checkForPreApproval(authorizationRequest,
                    (Authentication) principal);
            //is this call necessary?
            boolean approved = oauthUserApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
            authorizationRequest.setApproved(approved);

            // Validation is all done, so we can check for auto approval...
            if (authorizationRequest.isApproved()) {
//                if (responseTypes.contains("token")) {
//                    return getImplicitGrantResponse(authorizationRequest);
//                }
                if (responseTypes.contains("code")) {
                    return new ModelAndView(getAuthorizationCodeResponse(authorizationRequest,
                            (Authentication) principal));
                }
            }

            // Place auth request into the model so that it is stored in the session
            // for approveOrDeny to use. That way we make sure that auth request comes from the session,
            // so any auth request parameters passed to approveOrDeny will be ignored and retrieved from the session.
            model.put("authorizationRequest", authorizationRequest);

            return getUserApprovalPageResponse(model, authorizationRequest, (Authentication) principal);

        } catch (RuntimeException e) {
            sessionStatus.setComplete();
            throw e;
        }

    }


    @RequestMapping(value = "/oauth/authorize", method = RequestMethod.POST, params = OAuth2Utils.USER_OAUTH_APPROVAL)
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
                              SessionStatus sessionStatus, Principal principal) {

        if (!(principal instanceof Authentication)) {
            sessionStatus.setComplete();
            throw new InsufficientAuthenticationException(
                    "User must be authenticated with Spring Security before authorizing an access token.");
        }

        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");

        if (authorizationRequest == null) {
            sessionStatus.setComplete();
            throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
        }

        try {
            Set<String> responseTypes = authorizationRequest.getResponseTypes();

            authorizationRequest.setApprovalParameters(approvalParameters);
            authorizationRequest = oauthUserApprovalHandler.updateAfterApproval(authorizationRequest,
                    (Authentication) principal);
            boolean approved = oauthUserApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
            authorizationRequest.setApproved(approved);

            if (authorizationRequest.getRedirectUri() == null) {
                sessionStatus.setComplete();
                throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
            }

            if (!authorizationRequest.isApproved()) {
                return new RedirectView(getUnsuccessfulRedirect(authorizationRequest,
                        new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")),
                        false, true, false);
            }

//            if (responseTypes.contains("token")) {
//                return getImplicitGrantResponse(authorizationRequest).getView();
//            }

            return getAuthorizationCodeResponse(authorizationRequest, (Authentication) principal);
        } finally {
            sessionStatus.setComplete();
        }

    }

    private View getAuthorizationCodeResponse(AuthorizationRequest authorizationRequest, Authentication authUser) {
        try {
            return new RedirectView(getSuccessfulRedirect(authorizationRequest,
                    generateCode(authorizationRequest, authUser)), false, true, false);
        } catch (OAuth2Exception e) {
            return new RedirectView(getUnsuccessfulRedirect(authorizationRequest, e, false), false, true, false);
        }
    }


    // We need explicit approval from the user.
    private ModelAndView getUserApprovalPageResponse(Map<String, Object> model,
                                                     AuthorizationRequest authorizationRequest, Authentication principal) {
        logger.debug("Loading user approval page: " + userApprovalPage);
        model.putAll(oauthUserApprovalHandler.getUserApprovalRequest(authorizationRequest, principal));
        return new ModelAndView(userApprovalPage, model);
    }

    private String getSuccessfulRedirect(AuthorizationRequest authorizationRequest, String authorizationCode) {

        if (authorizationCode == null) {
            throw new IllegalStateException("No authorization code found in the current request scope.");
        }

        Map<String, String> query = new LinkedHashMap<String, String>();
        query.put("code", authorizationCode);

        String state = authorizationRequest.getState();
        if (state != null) {
            query.put("state", state);
        }

        return append(authorizationRequest.getRedirectUri(), query, false);
    }


    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication)
            throws AuthenticationException {

        try {

            OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authorizationRequest);

            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            String code = authorizationCodeService.createAuthorizationCode(combinedAuth);

            return code;

        } catch (OAuth2Exception e) {

            if (authorizationRequest.getState() != null) {
                e.addAdditionalInformation("state", authorizationRequest.getState());
            }

            throw e;

        }
    }

    private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure,
                                           boolean fragment) {

        if (authorizationRequest == null || authorizationRequest.getRedirectUri() == null) {
            // we have no redirect for the user. very sad.
            throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
        }

        Map<String, String> query = new LinkedHashMap<String, String>();

        query.put("error", failure.getOAuth2ErrorCode());
        query.put("error_description", failure.getMessage());

        if (authorizationRequest.getState() != null) {
            query.put("state", authorizationRequest.getState());
        }

        if (failure.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> additionalInfo : failure.getAdditionalInformation().entrySet()) {
                query.put(additionalInfo.getKey(), additionalInfo.getValue());
            }
        }

        return append(authorizationRequest.getRedirectUri(), query, fragment);

    }

    private String append(String base, Map<String, ?> query, boolean fragment) {
        return append(base, query, null, fragment);
    }

    private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {

        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
        URI redirectUri;
        try {
            // assume it's encoded to start with (if it came in over the wire)
            redirectUri = builder.build(true).toUri();
        } catch (Exception e) {
            // ... but allow client registrations to contain hard-coded non-encoded values
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                String append = redirectUri.getFragment();
                values.append(append);
            }
            for (String key : query.keySet()) {
                if (values.length() > 0) {
                    values.append("&");
                }
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                values.append(name + "={" + key + "}");
            }
            if (values.length() > 0) {
                template.fragment(values.toString());
            }
            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for (String key : query.keySet()) {
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                template.queryParam(name, "{" + key + "}");
            }
            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }

        return builder.build().toUriString();

    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ModelAndView handleClientRegistrationException(Exception e, ServletWebRequest webRequest) throws Exception {
        logger.info("Handling ClientRegistrationException error: " + e.getMessage());
        return handleException(new BadClientCredentialsException(), webRequest);
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ModelAndView handleOAuth2Exception(OAuth2Exception e, ServletWebRequest webRequest) throws Exception {
        logger.info("Handling OAuth2 error: " + e.getSummary());
        return handleException(e, webRequest);
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
    public ModelAndView handleHttpSessionRequiredException(HttpSessionRequiredException e, ServletWebRequest webRequest)
            throws Exception {
        logger.info("Handling Session required error: " + e.getMessage());
        return handleException(new AccessDeniedException("Could not obtain authorization request from session", e),
                webRequest);
    }

    private ModelAndView handleException(Exception e, ServletWebRequest webRequest) throws Exception {

        ResponseEntity<OAuth2Exception> translate = providerExceptionHandler.translate(e);
        webRequest.getResponse().setStatus(translate.getStatusCode().value());

        if (e instanceof ClientAuthenticationException || e instanceof RedirectMismatchException) {
            return new ModelAndView(errorPage, Collections.singletonMap("error", translate.getBody()));
        }

        AuthorizationRequest authorizationRequest = null;
        try {
            authorizationRequest = getAuthorizationRequestForError(webRequest);
            String requestedRedirectParam = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
            String requestedRedirect = redirectResolver.resolveRedirect(requestedRedirectParam,
                    clientDetailsService.loadClientByClientId(authorizationRequest.getClientId()));
            authorizationRequest.setRedirectUri(requestedRedirect);
            String redirect = getUnsuccessfulRedirect(authorizationRequest, translate.getBody(), authorizationRequest
                    .getResponseTypes().contains("token"));
            return new ModelAndView(new RedirectView(redirect, false, true, false));
        } catch (OAuth2Exception ex) {
            // If an AuthorizationRequest cannot be created from the incoming parameters it must be
            // an error. OAuth2Exception can be handled this way. Other exceptions will generate a standard 500
            // response.
            return new ModelAndView(errorPage, Collections.singletonMap("error", translate.getBody()));
        }

    }

    private AuthorizationRequest getAuthorizationRequestForError(ServletWebRequest webRequest) {

        // If it's already there then we are in the approveOrDeny phase and we can use the saved request
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) sessionAttributeStore.retrieveAttribute(
                webRequest, "authorizationRequest");
        if (authorizationRequest != null) {
            return authorizationRequest;
        }

        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String[]> map = webRequest.getParameterMap();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            if (values != null && values.length > 0) {
                parameters.put(key, values[0]);
            }
        }

        try {
            return oAuth2RequestFactory.createAuthorizationRequest(parameters);
        } catch (Exception e) {
            return oAuth2RequestFactory.createAuthorizationRequest(parameters);
        }

    }


}
