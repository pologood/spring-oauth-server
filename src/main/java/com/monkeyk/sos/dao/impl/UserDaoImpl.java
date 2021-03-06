package com.monkeyk.sos.dao.impl;

import com.monkeyk.sos.dao.IUserDao;
import com.monkeyk.sos.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by poppet on 16/10/24.
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDao<User, Integer> implements IUserDao {

    private final String namespace = User.class.getName();

    @Override
    public User findByUsername(String userName) {
        return sqlSession.selectOne(namespace + ".findByUsername", userName);
    }

    @Override
    public User findByGuid(String guid) {

        User user = sqlSession.selectOne(namespace + ".findByGuid", guid);

        return user;
    }

    @Override
    public List<User> findUsersByUsername(String username) {
        return sqlSession.selectList(namespace + ".findUsersByUsername", username);
    }

    @Override
    public User saveUser(User user) {
        return super.save(user);
    }

    @Override
    public void update(User user) {

    }
}
