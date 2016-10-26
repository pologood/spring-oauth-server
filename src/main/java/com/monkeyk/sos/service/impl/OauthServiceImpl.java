package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IOauthClientDetailsDao;
import com.monkeyk.sos.domain.OauthClientDetailsDto;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OAuth 业务处理服务对象, 事务拦截也加在这一层
 *
 * @author yaoguang.du@duolabao.com
 */
@Service("oauthService")
public class OauthServiceImpl implements OauthService {

    private static final Logger logger = LoggerFactory.getLogger(OauthServiceImpl.class);

    @Autowired
    private IOauthClientDetailsDao oauthClientDetailsDao;

    @Override
    public OauthClientDetails loadOauthClientDetails(String clientId) {
        try {
            return oauthClientDetailsDao.findById(clientId);

        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return null;
    }

    @Override
    public List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos() {
        try {
            List<OauthClientDetails> clientDetailses = oauthClientDetailsDao.findAll();
            return OauthClientDetailsDto.toDtos(clientDetailses);
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }
        return new ArrayList<>();

    }

    @Override
    public void archiveOauthClientDetails(String clientId) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("clientId", clientId);
            params.put("archived", true);

            oauthClientDetailsDao.updateOauthClientDetailsArchive(params);
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }

    @Override
    public OauthClientDetailsDto loadOauthClientDetailsDto(String clientId) {
        try {
            final OauthClientDetails oauthClientDetails = oauthClientDetailsDao.findById(clientId);
            return oauthClientDetails != null ? new OauthClientDetailsDto(oauthClientDetails) : null;
        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

        return null;
    }

    @Override
    public void registerClientDetails(OauthClientDetailsDto formDto) {
        try {
            OauthClientDetails clientDetails = formDto.createDomain();
            oauthClientDetailsDao.save(clientDetails);

        } catch (Exception e) {
            logger.error("exception occurred", e);
        }

    }
}