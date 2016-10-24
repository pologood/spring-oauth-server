package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.dao.IOauthClientDetailsDao;
import com.monkeyk.sos.domain.OauthClientDetailsDto;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.domain.oauth.OauthRepository;
import com.monkeyk.sos.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IOauthClientDetailsDao oauthClientDetailsDao;

    @Override
    public OauthClientDetails loadOauthClientDetails(String clientId) {
        return oauthClientDetailsDao.findById(clientId);
    }

    @Override
    public List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos() {
        List<OauthClientDetails> clientDetailses = oauthClientDetailsDao.findAll();
        return OauthClientDetailsDto.toDtos(clientDetailses);
    }

    @Override
    public void archiveOauthClientDetails(String clientId) {

        Map<String, Object> params = new HashMap<>();
        params.put("clientId", clientId);
        params.put("archived", true);

        oauthClientDetailsDao.updateOauthClientDetailsArchive(params);
    }

    @Override
    public OauthClientDetailsDto loadOauthClientDetailsDto(String clientId) {
        final OauthClientDetails oauthClientDetails = oauthClientDetailsDao.findById(clientId);
        return oauthClientDetails != null ? new OauthClientDetailsDto(oauthClientDetails) : null;
    }

    @Override
    public void registerClientDetails(OauthClientDetailsDto formDto) {
        OauthClientDetails clientDetails = formDto.createDomain();
        oauthClientDetailsDao.save(clientDetails);
    }
}