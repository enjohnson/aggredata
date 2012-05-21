/*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.GatewayDAO;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml"
})

public class GatewayServiceTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected GatewayService gatewayService;

    @Autowired
    protected GatewayDAO gatewayDAO;

    public static User testUser;
    public static User testUserMember;
    public static Group testGroup;
    public static String userEmailAddress;
    public static String groupName;
    public static Gateway gateway;

    @Before
    public void setUp() throws Exception {

        //Clean up old test data.
        Gateway oldGateway = new Gateway();
        oldGateway.setId(Long.parseLong("AAAAAA", 16));
        gatewayDAO.delete(oldGateway);


        userEmailAddress = TestUtil.getUniqueKey() + "@theenergydetective.com";
        groupName = TestUtil.getUniqueKey();

        testUser = new User();
        testUser.setUsername(userEmailAddress);
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.ROLE_USER);
        testUser.setAccountState(User.STATE_ENABLED);
        userService.createUser(testUser);
        testUser = userService.getUserByUserName(userEmailAddress);
        Assert.assertNotNull(testUser);
        groupService.createGroup(testUser, groupName);
        testGroup = groupService.getOwnedByUser(testUser).get(0);
        Assert.assertNotNull(testGroup);
        gateway = gatewayService.createGateway(testGroup, testUser, "AAAAAA", "AAAAAA");
    }

    @After
    public void tearDown() throws Exception {
        groupService.deleteGroup(testGroup);
        userService.deleteUser(testUser);
        gatewayService.deleteGateway(gateway);
    }


    @Test
    public void testAddGateway() {
        Assert.assertNotNull(testUser);
        Assert.assertNotNull(testGroup);
        Assert.assertNotNull(gateway);
   }




}