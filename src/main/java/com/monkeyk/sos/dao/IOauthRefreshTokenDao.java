package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.OauthAccessToken;
import com.monkeyk.sos.domain.OauthRefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * Created by poppet on 16/10/25.
 */
public interface IOauthRefreshTokenDao extends IBaseDao<OauthRefreshToken, String> {

    public OauthRefreshToken findById(String tokenId);

    public OAuth2RefreshToken findByToken(String tokenValue);

    public void deleteById(String tokenId);

    public OAuth2RefreshToken findRefreshToken(String token);

    public void removeFreshToken(String token);

}
