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


    public static User testUser;
    public static User testUserMember;
    public static Group testGroup;


    @Before
    public void setUp() throws Exception {
        User oldDataUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");
        if (oldDataUser != null) userService.deleteUser(oldDataUser);


        testUser = new User();
        testUser.setUsername("grouptestuser@theenergydetective.com");
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.ROLE_USER);
        testUser.setState(true);
        userService.createUser(testUser);
        testUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");

        testUserMember = new User();
        testUserMember.setUsername("grouptestmember@theenergydetective.com");
        testUserMember.setRole(User.ROLE_USER);
        testUserMember.setState(true);
        testUserMember  = userService.createUser(testUserMember);


        testGroup = groupService.createGroup(testUser, "Test Group");

    }

    @After
    public void tearDown() throws Exception {
        groupService.deleteGroup(testGroup);
        userService.deleteUser(testUserMember);
        userService.deleteUser(testUser);
    }


    @Test
    public void testAddMember() {
        groupService.addUserToGroup(testUserMember, testGroup, Group.Role.MEMBER);

        Group testGroup2 = groupService.getByUser(testUserMember).get(0);
        Assert.assertEquals(testGroup2.getDescription(), "Default Group");
        Assert.assertEquals(testGroup2.getRole(), Group.Role.OWNER);

        Group testGroup2b = groupService.getByUser(testUserMember).get(1);
        Assert.assertEquals(testGroup2b.getDescription(), "Test Group");
        Assert.assertEquals(testGroup2b.getRole(), Group.Role.MEMBER);


        groupService.changeUserRole(testUserMember, testGroup, Group.Role.READONLY);
        Group testGroup3 = groupService.getByUser(testUserMember).get(1);
        Assert.assertEquals(testGroup3.getDescription(), "Test Group");
        Assert.assertEquals(testGroup3.getRole(), Group.Role.READONLY);

        groupService.removeUserFromGroup(testUserMember, testGroup);
        Assert.assertEquals(groupService.getByUser(testUserMember).size(), 1);
    }

}