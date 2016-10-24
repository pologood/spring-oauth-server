package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.domain.user.Privilege;

import java.util.List;
import java.util.Map;

public interface IOauthClientDetailsDao extends IBaseDao<OauthClientDetails, String> {

    OauthClientDetails findById(String clientId);

    List<OauthClientDetails> findAll();

    void updateOauthClientDetailsArchive(Map<String, Object> params);

}
