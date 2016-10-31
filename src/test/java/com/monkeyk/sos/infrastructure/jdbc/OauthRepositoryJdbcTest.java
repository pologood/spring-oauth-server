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

import com.monkeyk.sos.dao.IOauthClientDetailsDao;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.infrastructure.AbstractRepositoryTest;
import com.monkeyk.sos.utils.GuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/*
  * @author Shengzhao Li
  */
public class OauthRepositoryJdbcTest extends AbstractRepositoryTest {


    @Resource
    private IOauthClientDetailsDao oauthClientDetailsDao;


    @Test
    public void findOauthClientDetails() {
        OauthClientDetails oauthClientDetails = oauthClientDetailsDao.findById("unity-client");
        assertNull(oauthClientDetails);

    }


    @Test
    public void saveOauthClientDetails() {

        final String clientId = GuidGenerator.generate();

        OauthClientDetails clientDetails = new OauthClientDetails();
        clientDetails.setAppNum(clientId);

        oauthClientDetailsDao.save(clientDetails);

        final OauthClientDetails oauthClientDetails = oauthClientDetailsDao.findById(clientId);
        assertNotNull(oauthClientDetails);
        assertNotNull(oauthClientDetails.getAppNum());
        assertNull(oauthClientDetails.getAppSecretKey());

    }

    @Test
    public void findAllOauthClientDetails() {
        final List<OauthClientDetails> allOauthClientDetails = oauthClientDetailsDao.findAll();
        assertNotNull(allOauthClientDetails);
        assertTrue(allOauthClientDetails.isEmpty());
    }

    @Test
    public void updateOauthClientDetailsArchive() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", "safasfdsdas");
        params.put("archived", true);

        oauthClientDetailsDao.updateOauthClientDetailsArchive(params);
    }


}