/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.dao.IUserDao;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.infrastructure.AbstractRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

/*
  * @author yaoguang.du@duolabao.com
  */
public class UserRepositoryJdbcTest extends AbstractRepositoryTest {


    @Resource
    private IUserDao userDao;


    @Test
    public void findByGuid() {
        User user = userDao.findByGuid("oood");
        assertNull(user);

        user = new User("user", "123", "123", "ewo@honyee.cc");
        userDao.saveUser(user);

        user = userDao.findByGuid(user.getGuid());
        assertNotNull(user);
        assertNotNull(user.getEmail());


    }

    @Test
    public void findUsersByUsername() {
        User user = userDao.findByGuid("oood");
        assertNull(user);

        user = new User("user", "123", "123", "ewo@honyee.cc");
        userDao.saveUser(user);

        final List<User> list = userDao.findUsersByUsername(user.getUsername());
        assertNotNull(list);

        assertEquals(list.size(), 1);

    }


    @Test
    public void updateUser() {
        User user = new User("user", "123", "123", "ewo@honyee.cc");
        userDao.saveUser(user);

        user = userDao.findByGuid(user.getGuid());
        assertNotNull(user);
        assertNotNull(user.getEmail());

        String newEmail = "test@honyee.cc";
        user.setEmail(newEmail).setPhone("12344444");
        userDao.update(user);

        user = userDao.findByGuid(user.getGuid());
        assertNotNull(user);
        assertEquals(user.getEmail(), newEmail);
    }


    @Test
    public void findByUsername() {
        String username = "user";
        User user = new User(username, "123", "123", "ewo@honyee.cc");
        userDao.saveUser(user);

        User result = userDao.findByUsername(username);
        assertNotNull(result);
    }


    /*
    * Run the test must initial db firstly
    * */
    @Test(enabled = false)
    public void testPrivilege() {

        String guid = "55b713df1c6f423e842ad68668523c49";
        final User user = userDao.findByGuid(guid);

        assertNotNull(user);
        assertEquals(user.getPrivileges().size(), 1);


    }


}