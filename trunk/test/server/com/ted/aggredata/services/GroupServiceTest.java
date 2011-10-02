/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package server.com.ted.aggredata.services;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import server.com.ted.aggredata.model.Group;
import server.com.ted.aggredata.model.User;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:resources/applicationContext.xml",
                                   "classpath:resources/applicationContext-Test.xml"
                    })

public class GroupServiceTest
{
    @Autowired
    protected  UserService userService;

    @Autowired
    protected GroupService groupService;

    public static User testUser;
    public static User testUserMember;



     private void setUpUser() {
        testUser = new User();
        testUser.setUsername("grouptestuser@theenergydetective.com");
        testUser.setPassword("aggredata");
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.Role.MEMBER);
        testUser.setState(true);
        userService.createUser(testUser);
        testUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");

         testUserMember = new User();
         testUserMember.setUsername("grouptestmember@theenergydetective.com");
         testUserMember.setPassword("aggredata");
         testUserMember.setDefaultGroupId(0);
         testUserMember.setRole(User.Role.MEMBER);
         testUserMember.setState(true);
         userService.createUser(testUserMember);
         testUserMember = userService.getUserByUserName("grouptestmember@theenergydetective.com");

     }

     @Test
     public void testCreate()
     {
         setUpUser();

         groupService.createGroup(testUser, "Test Group");
         Group testGroup = groupService.getByUser(testUser).get(0);
         Assert.assertEquals(testGroup.getDescription(), "Test Group");
         Assert.assertEquals(testGroup.getRole(), Group.Role.ADMIN);
     }

    @Test
    public void testAddMember()
    {
        Group testGroup = groupService.getByUser(testUser).get(0);
        groupService.addUserToGroup(testUserMember, testGroup, Group.Role.MEMBER);

        Group testGroup2 = groupService.getByUser(testUserMember).get(0);
        Assert.assertEquals(testGroup2.getDescription(), "Test Group");
        Assert.assertEquals(testGroup2.getRole(), Group.Role.MEMBER);

        groupService.changeUserRole(testUserMember, testGroup, Group.Role.READONLY);
        Group testGroup3 = groupService.getByUser(testUserMember).get(0);
        Assert.assertEquals(testGroup3.getDescription(), "Test Group");
        Assert.assertEquals(testGroup3.getRole(), Group.Role.READONLY);

        groupService.removeUserFromGroup(testUserMember, testGroup);
        Assert.assertEquals(groupService.getByUser(testUserMember).size(), 0);
    }



    @Test
    public void testDelete()
    {
        Group testGroup = groupService.getByUser(testUser).get(0);
        System.out.println("Testing the delete:" + testGroup);
        groupService.deleteGroup(testGroup);
        List<Group> groupList = groupService.getByUser(testUser);
        Assert.assertEquals(groupList.size(), 0);

        //clean up
        userService.deleteUser(testUser);
        userService.deleteUser(testUserMember);
    }

}