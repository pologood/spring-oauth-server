package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IAuthCodeDao;
import com.monkeyk.sos.domain.AuthCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import javax.annotation.Resource;

/**
 * Created by poppet on 16/10/25.
 */
public class AuthorizationCodeServiceImpl extends RandomValueAuthorizationCodeServices {


    private static final Logger logger = LoggerFactory.getLogger(AuthorizationCodeServiceImpl.class);

    @Resource
    IAuthCodeDao authCodeDao;

    @Override
    protected void store(String s, OAuth2Authentication oAuth2Authentication) {


        try {
            AuthCode ac = new AuthCode();
            ac.setCode(s);
            ac.setNum(s);
            ac.setAuthentication(SerializationUtils.serialize(oAuth2Authentication));
            authCodeDao.save(ac);
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }

    @Override
    protected OAuth2Authentication remove(String s) {
        //authCodeDao.remove(s);

        OAuth2Authentication authentication = null;

        try {
            AuthCode ac = authCodeDao.findByCode(s);
            if (ac != null) {
                authentication = (OAuth2Authentication) SerializationUtils.deserialize(ac.getAuthentication());
            }
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }


        return authentication;
    }
}
