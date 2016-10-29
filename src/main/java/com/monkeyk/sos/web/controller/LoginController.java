package com.monkeyk.sos.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by poppet on 16/10/28.
 */
@Controller
public class LoginController implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return authenticationDetailsSource;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {

        logger.info("executing user defined login method");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        try {
            Authentication authentication = getAuthenticationManager().authenticate(authRequest);

        } catch (Exception e) {
            logger.error("error occurred while logging in ",e);
        }

        return "../../index";

    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.authenticationManager == null) {
            this.authenticationManager = (AuthenticationManager) applicationContext.getBean("authenticationManager");
        }

//        = new WebAuthenticationDetailsSource();
        if (authenticationDetailsSource == null) {
            authenticationDetailsSource = new WebAuthenticationDetailsSource();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
