package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IAuthCodeDao;
import com.monkeyk.sos.dao.IPrivilegeDao;
import com.monkeyk.sos.domain.AuthCode;
import com.monkeyk.sos.domain.user.Privilege;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("authCodeDao")
public class AuthCodeDaoImpl extends BaseDao<AuthCode, String> implements IAuthCodeDao {

    private final String namespace = AuthCode.class.getName();


    @Override
    public void update(AuthCode authCode) {
    }

    @Override
    public void remove(String code) {
        sqlSession.delete(namespace + ".deleteByCode", code);
    }

    @Override
    public AuthCode findByCode(String code) {
        return sqlSession.selectOne(namespace+".findByCode",code);
    }
}
