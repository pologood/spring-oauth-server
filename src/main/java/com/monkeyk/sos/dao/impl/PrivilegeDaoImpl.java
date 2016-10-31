package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IPrivilegeDao;
import com.monkeyk.sos.domain.user.Privilege;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("privilegeDao")
public class PrivilegeDaoImpl extends BaseDao<Privilege, Integer> implements IPrivilegeDao {

    private final String namespace = Privilege.class.getName();


    @Override
    public List<Privilege> findByUserNum(String userNum) {
        return sqlSession.selectList(namespace + ".findByUserNum", userNum);
    }

    @Override
    public void savePrivilege(Map<String, Object> params) {
        sqlSession.insert(namespace + ".insert", params);
    }

    @Override
    public Privilege save(Privilege privilege) {
        return null;
    }

    @Override
    public void update(Privilege privilege) {

    }
}
