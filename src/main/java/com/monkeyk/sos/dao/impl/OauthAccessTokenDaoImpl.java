package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IOauthAccessTokenDao;
import com.monkeyk.sos.domain.OauthAccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("oauthAccessTokenDao")
public class OauthAccessTokenDaoImpl extends BaseDao<OauthAccessToken, String> implements IOauthAccessTokenDao {

    private final String namespace = OauthAccessToken.class.getName();

    @Override
    public OAuth2AccessToken findByToken(String tokenValue) {
        return sqlSession.selectOne(namespace + ".findByToken", tokenValue);
    }

    @Override
    public OauthAccessToken findById(String s) {
        return super.findById(s);
    }

    @Override
    public void deleteById(String tokenId) {
        sqlSession.delete(namespace + ".deleteById", tokenId);
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        sqlSession.delete(namespace + ".deleteByRefreshToken", refreshToken);
    }

    @Override
    public OAuth2RefreshToken findRefreshToken(String token) {
        return null;
    }

    @Override
    public void removeFreshToken(String token) {
        sqlSession.delete(namespace + ".deleteByFreshToken", token);
    }

    @Override
    public OauthAccessToken findByAuthenticationId(String authenticationId) {
        return sqlSession.selectOne(namespace + ".findByAuthenticationId", authenticationId);
    }

    @Override
    public List<OauthAccessToken> findByParam(Map<String, Object> params) {
        return sqlSession.selectList(namespace + ".findByParams", params);
    }

    @Override
    public void update(OauthAccessToken oauthAccessToken) {

    }
}
