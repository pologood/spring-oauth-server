package com.monkeyk.sos.domain;

import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.infrastructure.DateUtils;
import com.monkeyk.sos.utils.GuidGenerator;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoguang.du@duolabao.com
 */
public class OauthClientDetailsDto implements Serializable {


    private static final long serialVersionUID = -690401014127462329L;
    private String createTime;
    private boolean archived;

    private String clientId = GuidGenerator.generate();
    private String resourceIds;

    private String clientSecret = GuidGenerator.generateClientSecret();

    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    // optional
    private String additionalInformation;

    private boolean trusted;

    public OauthClientDetailsDto() {
    }

    public OauthClientDetailsDto(OauthClientDetails clientDetails) {
        this.clientId = clientDetails.getClientId();
        this.clientSecret = clientDetails.getClientSecret();
        this.scope = clientDetails.getScope();

        this.createTime = DateUtils.toDateTime(clientDetails.createTime());
        this.archived = clientDetails.isArchived();
        this.resourceIds = clientDetails.getResourceIds();

        this.webServerRedirectUri = clientDetails.getWebServerRedirectUri();
        this.authorities = clientDetails.getAuthorities();
        this.accessTokenValidity = clientDetails.getAccessTokenValidity();

        this.refreshTokenValidity = clientDetails.getRefreshTokenValidity();
        this.additionalInformation = clientDetails.getAdditionalInformation();
        this.trusted = clientDetails.isTrusted();

        this.authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }


    public String getScopeWithBlank() {
        if (scope != null && scope.contains(",")) {
            return scope.replaceAll(",", " ");
        }
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public static List<OauthClientDetailsDto> toDtos(List<OauthClientDetails> clientDetailses) {
        List<OauthClientDetailsDto> dtos = new ArrayList<>(clientDetailses.size());
        for (OauthClientDetails clientDetailse : clientDetailses) {
            dtos.add(new OauthClientDetailsDto(clientDetailse));
        }
        return dtos;
    }


    public boolean isContainsAuthorizationCode() {
        return this.authorizedGrantTypes.contains("authorization_code");
    }

    public boolean isContainsPassword() {
        return this.authorizedGrantTypes.contains("password");
    }

    public boolean isContainsImplicit() {
        return this.authorizedGrantTypes.contains("implicit");
    }

    public boolean isContainsClientCredentials() {
        return this.authorizedGrantTypes.contains("client_credentials");
    }

    public boolean isContainsRefreshToken() {
        return this.authorizedGrantTypes.contains("refresh_token");
    }


    public OauthClientDetails createDomain() {
        OauthClientDetails clientDetails = new OauthClientDetails()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setResourceIds(resourceIds)
                .setAuthorizedGrantTypes(authorizedGrantTypes)
                .setScope(scope);

        if (StringUtils.isNotEmpty(webServerRedirectUri)) {
            clientDetails.setWebServerRedirectUri(webServerRedirectUri);
        }

        if (StringUtils.isNotEmpty(authorities)) {
            clientDetails.setAuthorities(authorities);
        }

        clientDetails.accessTokenValidity(accessTokenValidity)
                .setRefreshTokenValidity(refreshTokenValidity)
                .setTrusted(trusted);

        if (StringUtils.isNotEmpty(additionalInformation)) {
            clientDetails.setAdditionalInformation(additionalInformation);
        }

        return clientDetails;
    }
}