package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.user.Privilege;

import java.util.List;

public interface IPrivilegeDao extends IBaseDao<Privilege, Integer> {

    public List<Privilege> findByUserId(int userId);
}
