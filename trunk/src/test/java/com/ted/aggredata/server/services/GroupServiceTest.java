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

public class GroupServiceTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected GatewayService gatewayService;

    public static User testUser;
    public static User testUserMember;
    public static Group testGroup;


    @Before
    public void setUp() throws Exception {
        User oldDataUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");
        if (oldDataUser != null) userService.deleteUser(oldDataUser);

        Gateway g = new Gateway();
        g.setId(Long.parseLong("AAAAA1", 16));
        gatewayService.deleteGateway(g);


        testUser = new User();
        testUser.setUsername("grouptestuser@theenergydetective.com");
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.ROLE_USER);
        testUser.setAccountState(User.STATE_ENABLED);
        userService.createUser(testUser, User.STATE_ENABLED);
        testUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");

        testUserMember = new User();
        testUserMember.setUsername("grouptestmember@theenergydetective.com");
        testUserMember.setRole(User.ROLE_USER);
        testUserMember.setAccountState(User.STATE_ENABLED);
        testUserMember  = userService.createUser(testUserMember, User.STATE_ENABLED);



        testGroup = groupService.createGroup(testUser, "Test Group");
        gatewayService.createGateway(testGroup, testUser, "AAAAA1", "AAAAA1");
    }

    @After
    public void tearDown() throws Exception {
        groupService.deleteGroup(testGroup);
        userService.deleteUser(testUserMember);
        userService.deleteUser(testUser);
    }


    @Test
    public void testAddMember() {


        groupService.addUserToGroup(testUserMember, testGroup, Group.Role.READONLY);

        Group testGroup2 = groupService.getOwnedByUser(testUserMember).get(0);
        Assert.assertEquals(testGroup2.getDescription(), "Default Group");
        Assert.assertEquals(testGroup2.getRole(), Group.Role.OWNER);

        Group testGroup2b = groupService.getByUserWithGateways(testUserMember).get(0);
        Assert.assertEquals(testGroup2b.getDescription(), "Test Group");
        Assert.assertEquals(testGroup2b.getRole(), Group.Role.READONLY);

        groupService.removeUserFromGroup(testUserMember, testGroup);
        Assert.assertEquals(groupService.getByUserWithGateways(testUserMember).size(), 0);
    }

}