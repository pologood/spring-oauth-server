package com.monkeyk.sos.domain.oauth;

import java.io.Serializable;
import java.util.Date;

import com.monkeyk.sos.infrastructure.DateUtils;

/**
 * 定义OAuth中的 Client, 也称 ClientDetails
 *
 * @author yaoguang.du@duolabao.com
 */
public class OauthClientDetails implements Serializable {


    private static final long serialVersionUID = -6947822646185526939L;

    private Date createTime = DateUtils.now();
    private boolean archived = false;

    private String clientId;
    private String resourceIds;

    private String clientSecret;
    /**
     * Available values: read,write
     */
    private String scope;

    /**
     * grant types include
     * "authorization_code", "password", "assertion", and "refresh_token".
     * Default value is "authorization_code,refresh_token".
     */
    private String authorizedGrantTypes = "authorization_code,refresh_token";

    /**
     * The re-direct URI(s) established during registration (optional, comma separated).
     */
    private String webServerRedirectUri;

    /**
     * Authorities that are granted to the client (comma-separated). Distinct from the authorities
     * granted to the user on behalf of whom the client is acting.
     * <p/>
     * For example: ROLE_USER
     */
    private String authorities;

    /**
     * The access token validity period in seconds (optional).
     * If unspecified a global default will be applied by the token services.
     */
    private Integer accessTokenValidity;

    /**
     * The refresh token validity period in seconds (optional).
     * If unspecified a global default will  be applied by the token services.
     */
    private Integer refreshTokenValidity;

    // optional
    private String additionalInformation;

    /**
     * The client is trusted or not. If it is trust, will skip approve step
     * default false.
     */
    private boolean trusted = false;

    /**
     * Value is 'true' or 'false',  default 'false'
     */
    private String autoApprove;

    public OauthClientDetails() {
    }

    public String getAutoApprove() {
        return autoApprove;
    }

    public OauthClientDetails setAutoApprove(String autoApprove) {
        this.autoApprove = autoApprove;
        return this;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public Date createTime() {
        return createTime;
    }

    public OauthClientDetails setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getClientId() {
        return clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OauthClientDetails");
        sb.append("{createTime=").append(createTime);
        sb.append(", archived=").append(archived);
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", resourceIds='").append(resourceIds).append('\'');
        sb.append(", clientSecret='").append(clientSecret).append('\'');
        sb.append(", scope='").append(scope).append('\'');
        sb.append(", authorizedGrantTypes='").append(authorizedGrantTypes).append('\'');
        sb.append(", webServerRedirectUri='").append(webServerRedirectUri).append('\'');
        sb.append(", authorities='").append(authorities).append('\'');
        sb.append(", accessTokenValidity=").append(accessTokenValidity);
        sb.append(", refreshTokenValidity=").append(refreshTokenValidity);
        sb.append(", additionalInformation='").append(additionalInformation).append('\'');
        sb.append(", trusted=").append(trusted);
        sb.append('}');
        return sb.toString();
    }


    public OauthClientDetails setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OauthClientDetails setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
        return this;
    }

    public OauthClientDetails setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public OauthClientDetails setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
        return this;
    }

    public OauthClientDetails setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public OauthClientDetails setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
        return this;
    }

    public OauthClientDetails setAuthorities(String authorities) {
        this.authorities = authorities;
        return this;
    }

    public OauthClientDetails accessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
        return this;
    }

    public OauthClientDetails setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
        return this;
    }

    public OauthClientDetails setTrusted(boolean trusted) {
        this.trusted = trusted;
        return this;
    }

    public OauthClientDetails setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public OauthClientDetails setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }
}