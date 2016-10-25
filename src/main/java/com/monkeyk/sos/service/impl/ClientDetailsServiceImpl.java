package com.monkeyk.sos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.monkeyk.sos.dao.IOauthClientDetailsDao;
import com.monkeyk.sos.domain.OauthClientDetailsDto;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * OAuth 业务处理服务对象, 事务拦截也加在这一层
 *
 * @author yaoguang.du@duolabao.com
 */

@Service("clientDetailsService")
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private IOauthClientDetailsDao oauthClientDetailsDao;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {


        OauthClientDetails details = oauthClientDetailsDao.findById(s);

        BaseClientDetails baseClientDetails = null;

        if (details != null) {

            //SET clientDetails value
            baseClientDetails = new BaseClientDetails(details.getClientId(), details.getResourceIds(), details.getScope(), details.getAuthorizedGrantTypes(), details.getAuthorities(), details.getWebServerRedirectUri());
            baseClientDetails.setClientSecret(details.getClientSecret());

            if (details.getAccessTokenValidity() != null && details.getAccessTokenValidity() > 0) {
                baseClientDetails.setAccessTokenValiditySeconds(details.getAccessTokenValidity());
            }

            if (details.getRefreshTokenValidity() != null && details.getRefreshTokenValidity() > 0) {
                baseClientDetails.setRefreshTokenValiditySeconds(details.getRefreshTokenValidity());
            }

            if (!StringUtils.isEmpty(details.getAdditionalInformation())) {
                baseClientDetails.setAdditionalInformation(JSONObject.parseObject(details.getAdditionalInformation()));
            }

            if (!StringUtils.isEmpty(details.getAutoApprove())) {
                baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(details.getAutoApprove()));
            }

        }

        return baseClientDetails;
    }
}