package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IAuthCodeDao;
import com.monkeyk.sos.domain.AuthCode;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import javax.annotation.Resource;

/**
 * Created by poppet on 16/10/25.
 */
public class AuthorizationCodeServiceImpl extends RandomValueAuthorizationCodeServices {


    @Resource
    IAuthCodeDao authCodeDao;

    @Override
    protected void store(String s, OAuth2Authentication oAuth2Authentication) {
        AuthCode ac = new AuthCode();
        ac.setCode(s);
        ac.setAuthentication(SerializationUtils.serialize(oAuth2Authentication));
        authCodeDao.save(ac);
    }

    @Override
    protected OAuth2Authentication remove(String s) {
        //authCodeDao.remove(s);

        AuthCode ac = authCodeDao.findByCode(s);
        OAuth2Authentication authentication = null;
        if (ac != null) {
            authentication = (OAuth2Authentication) SerializationUtils.deserialize(ac.getAuthentication());
        }

        return authentication;
    }
}
