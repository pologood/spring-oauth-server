package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.OauthAccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.List;
import java.util.Map;

/**
 * Created by poppet on 16/10/25.
 */
public interface IOauthAccessTokenDao extends IBaseDao<OauthAccessToken, String> {

    public OauthAccessToken findById(String tokenId);

    public OAuth2AccessToken findByToken(String tokenValue);

    public void deleteById(String tokenId);

    public void deleteByRefreshToken(String refreshToken);

    public OAuth2RefreshToken findRefreshToken(String token);

    public void removeFreshToken(String token);

    public OauthAccessToken findByAuthenticationId(String authenticationId);

    public List<OauthAccessToken> findByParam(Map<String, Object> params);

}
