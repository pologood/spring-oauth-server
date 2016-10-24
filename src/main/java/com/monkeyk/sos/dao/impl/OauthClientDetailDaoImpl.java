package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IOauthClientDetailsDao;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("oauthClientDetailsDao")
public class OauthClientDetailDaoImpl extends BaseDao<OauthClientDetails, String> implements IOauthClientDetailsDao {

    private final String namespace = OauthClientDetails.class.getName();

    @Override
    public List<OauthClientDetails> findAll() {
        return sqlSession.selectList(namespace + ".findAll");
    }

    @Override
    public void updateOauthClientDetailsArchive(Map<String, Object> params) {
        sqlSession.update(namespace + ".update", params);
    }

    @Override
    public void update(OauthClientDetails oauthClientDetails) {

    }
}
