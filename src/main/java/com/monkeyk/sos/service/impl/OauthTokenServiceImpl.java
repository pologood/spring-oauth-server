package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IOauthAccessTokenDao;
import com.monkeyk.sos.dao.IOauthRefreshTokenDao;
import com.monkeyk.sos.domain.OauthAccessToken;
import com.monkeyk.sos.domain.OauthRefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by poppet on 16/10/25.
 */

public class OauthTokenServiceImpl implements TokenStore {

    private static final Logger logger = LoggerFactory.getLogger(OauthTokenServiceImpl.class);

    @Resource
    IOauthAccessTokenDao oauthAccessTokenDao;

    @Resource
    IOauthRefreshTokenDao oauthRefreshTokenDao;

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Resource
    AuthenticationKeyGenerator authenticationKeyGenerator;


    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        try {
            return readAuthentication((String) token.getValue());

        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenId) {

        OAuth2Authentication authentication = null;
        try {
            String token = extractTokenKey(tokenId);

            if (!StringUtils.isEmpty(token)) {
                OauthAccessToken oauthAccessToken = oauthAccessTokenDao.findById(token);
                authentication = (OAuth2Authentication) SerializationUtils.deserialize(oauthAccessToken.getAuthentication());
            }

        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to find access token for token {}", tokenId, e);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to deserialize authentication for {}" + tokenId, e);
            this.removeAccessToken(tokenId);
        } catch (Exception e) {
            logger.error("exception occurred", e);
            this.removeAccessToken(tokenId);
        }

        return authentication;
    }

    public void removeAccessToken(String tokenId) {
        try {
            String token = extractTokenKey(tokenId);

            if (!StringUtils.isEmpty(token)) {
                oauthAccessTokenDao.deleteById(token);
            }
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        try {
            String refreshToken = null;

            if (token.getRefreshToken() != null) {
                refreshToken = token.getRefreshToken().getValue();
            }

            if (this.readAccessToken(token.getValue()) != null) {
                this.removeAccessToken((String) token.getValue());
            }

            OauthAccessToken oauthAccessToken = new OauthAccessToken();
            oauthAccessToken.setTokenId(extractTokenKey(token.getValue()));
            oauthAccessToken.setToken(SerializationUtils.serialize(token));
            oauthAccessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
            oauthAccessToken.setUserName(authentication.isClientOnly() ? null : authentication.getName());
            oauthAccessToken.setAppNum(authentication.getOAuth2Request().getClientId());
            oauthAccessToken.setAuthentication(SerializationUtils.serialize(authentication));
            oauthAccessToken.setRefreshToken(extractTokenKey(refreshToken));
            oauthAccessTokenDao.save(oauthAccessToken);
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenId) {

        OAuth2AccessToken accessToken = null;

        try {

            String token = extractTokenKey(tokenId);
            if (!StringUtils.isEmpty(token)) {
                OauthAccessToken oauthAccessToken = oauthAccessTokenDao.findById(token);
                accessToken = (OAuth2AccessToken) SerializationUtils.deserialize(oauthAccessToken.getToken());
            }

        } catch (EmptyResultDataAccessException var4) {
            logger.error("Failed to find access token for token {}" + tokenId);
        } catch (IllegalArgumentException var5) {
            logger.error("Failed to deserialize access token for {}" + tokenId, var5);
            this.removeAccessToken((String) tokenId);
        } catch (Exception e) {
            logger.error("exception occurred", e);
            this.removeAccessToken((String) tokenId);
        }

        return accessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {

        removeAccessToken((String) token.getValue());
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication oAuth2Authentication) {
        try {
            OauthRefreshToken oauthRefreshToken = new OauthRefreshToken();
            oauthRefreshToken.setTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
            oauthRefreshToken.setToken(SerializationUtils.serialize(oAuth2RefreshToken));
            oauthRefreshToken.setAuthentication(SerializationUtils.serialize(oAuth2Authentication));
            oauthRefreshTokenDao.save(oauthRefreshToken);

        } catch (Exception e) {
            logger.error("exception occurred", e);
        }


    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenId) {
        OAuth2RefreshToken refreshToken = null;
        try {
            String token = extractTokenKey(tokenId);

            if (!StringUtils.isEmpty(token)) {

                OauthRefreshToken oauthRefreshToken = oauthRefreshTokenDao.findById(token);
                refreshToken = (OAuth2RefreshToken) SerializationUtils.deserialize(oauthRefreshToken.getToken());
            }

        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to find refresh token for token {}" + tokenId, e);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to deserialize refresh token for token {}" + tokenId, e);
            this.removeRefreshToken(tokenId);
        } catch (Exception e) {
            logger.error("exception occurred", e);
            this.removeRefreshToken(tokenId);
        }

        return refreshToken;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken((String) token.getValue());
    }


    public OAuth2Authentication readAuthenticationForRefreshToken(String tokenId) {
        OAuth2Authentication authentication = null;

        try {
            String token = extractTokenKey(tokenId);

            if (!StringUtils.isEmpty(token)) {

                OauthRefreshToken oauthRefreshToken = oauthRefreshTokenDao.findById(token);
                authentication = (OAuth2Authentication) SerializationUtils.deserialize(oauthRefreshToken.getAuthentication());
            }
        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to find access token for token {}" + tokenId);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to deserialize access token for {}" + tokenId, e);
            this.removeRefreshToken((String) tokenId);
        } catch (Exception e) {
            logger.error("exception occurred", e);
            this.removeRefreshToken((String) tokenId);
        }

        return authentication;
    }

    public void removeRefreshToken(String tokenId) {
        try {
            String token = extractTokenKey(tokenId);
            if (!StringUtils.isEmpty(token)) {
                oauthRefreshTokenDao.deleteById(token);
            }
        } catch (Exception e) {
            logger.error("exception error", e);
        }

    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken((String) token.getValue());
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        try {
            if (!StringUtils.isEmpty(refreshToken)) {

                String token = extractTokenKey(refreshToken);
                oauthAccessTokenDao.deleteByRefreshToken(token);
            }

        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {

        OAuth2AccessToken accessToken = null;
        String key = authenticationKeyGenerator.extractKey(authentication);

        try {
            if (!StringUtils.isEmpty(key)) {

                OauthAccessToken oauthAccessToken = oauthAccessTokenDao.findByAuthenticationId(key);

                if (oauthAccessToken != null) {
                    accessToken = (OAuth2AccessToken) SerializationUtils.deserialize(oauthAccessToken.getToken());
                }
            }

        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to find access token for authentication {}" + authentication);
        } catch (IllegalArgumentException e) {
            logger.error("Could not extract access token for authentication {}" + authentication, e);
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

        if (accessToken != null && !key.equals(authenticationKeyGenerator.extractKey(this.readAuthentication((String) accessToken.getValue())))) {
            this.removeAccessToken((String) accessToken.getValue());
            this.storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List accessTokens = new ArrayList();

        try {

            Map<String, Object> params = new HashMap<>();
            params.put("appNum", clientId);
            params.put("userName", userName);

            accessTokens = oauthAccessTokenDao.findByParam(params);

        } catch (EmptyResultDataAccessException var5) {
            logger.error("Failed to find access token for clientId {} and userName {} ", clientId, userName);
        }

        return accessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List accessTokens = new ArrayList();

        try {

            Map<String, Object> params = new HashMap<>();
            params.put("appNum", clientId);

            accessTokens = oauthAccessTokenDao.findByParam(params);

        } catch (EmptyResultDataAccessException var5) {
            logger.error("Failed to find access token for clientId {}  ", clientId);
        }

        return accessTokens;

    }


    public Collection<OAuth2AccessToken> findTokensByUserName(String userName) {
        List accessTokens = new ArrayList();

        try {

            Map<String, Object> params = new HashMap<>();
            params.put("userName", userName);

            accessTokens = oauthAccessTokenDao.findByParam(params);

        } catch (EmptyResultDataAccessException var5) {
            logger.error("Failed to find access token for userName {}  ", userName);
        }

        return accessTokens;

    }


    private String extractTokenKey(String value) {
        try {
            if (value == null) {
                return "";
            } else {
                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException var5) {
                    throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
                }

                try {
                    byte[] e = digest.digest(value.getBytes("UTF-8"));
                    return String.format("%032x", new Object[]{new BigInteger(1, e)});
                } catch (UnsupportedEncodingException var4) {
                    throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
                }
            }
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;

    }
}
