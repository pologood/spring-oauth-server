package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IOauthAccessTokenDao;
import com.monkeyk.sos.dao.IOauthRefreshTokenDao;
import com.monkeyk.sos.domain.OauthAccessToken;
import com.monkeyk.sos.domain.OauthRefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Repository;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("oauthRefreshTokenDao")
public class OauthRefreshTokenDaoImpl extends BaseDao<OauthRefreshToken, String> implements IOauthRefreshTokenDao {

    private final String namespace = OauthRefreshToken.class.getName();


    @Override
    public OAuth2RefreshToken findByToken(String tokenValue) {
        return null;
    }

    @Override
    public OAuth2RefreshToken findRefreshToken(String token) {
        return null;
    }

    @Override
    public void removeFreshToken(String token) {

    }

    @Override
    public void update(OauthRefreshToken oauthRefreshToken) {

    }
}
