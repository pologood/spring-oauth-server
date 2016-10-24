package com.monkeyk.sos.dao;

import com.monkeyk.sos.domain.user.User;

import java.util.List;

public interface IUserDao extends IBaseDao<User, Integer> {

    public User findByUsername(String userName);

    public User findByGuid(String guid);

    public List<User> findUsersByUsername(String userName);

    public User saveUser(User user);
}
