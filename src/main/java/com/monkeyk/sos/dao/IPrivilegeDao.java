package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.user.Privilege;

import java.util.List;
import java.util.Map;

public interface IPrivilegeDao extends IBaseDao<Privilege, Integer> {

    public List<Privilege> findByUserNum(String userNum);

    public void savePrivilege(Map<String, Object> params);
}
