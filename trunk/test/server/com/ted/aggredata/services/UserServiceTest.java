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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import client.com.ted.aggredata.model.User;

/**
 * User Service Unit test
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:resources/applicationContext.xml",
                                   "classpath:resources/applicationContext-Test.xml"
                    })


public class UserServiceTest {


    static String testUserName = "testuser@theenergydetective.com";
    static String testUserName2 = "testuser2@theenergydetective.com";


    @Autowired
    protected UserService userService;


    @Before
     public void setUp() throws Exception {
     }

     @After
     public void tearDown() throws Exception {
     }

    @Test
    public void testUserCreate() {

        User testUser = new User();
        testUser.setUsername(testUserName);
        testUser.setPassword("aggredata");
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.Role.MEMBER);
        testUser.setState(true);

        //Save the user
        userService.createUser(testUser);

        //Load the user
        testUser = userService.getUserByUserName(testUser.getUsername());
        Assert.assertNotNull(testUser);

    }

    @Test
    public void testChangePassword() {
        User testUser = userService.getUserByUserName(testUserName);
        testUser = userService.changePassword(testUser, "password2");
        Assert.assertEquals(testUser.getPassword(), "password2");
        testUser = userService.getUserByUserName(testUserName);
        Assert.assertEquals(testUser.getPassword(), "password2");
    }

    @Test
    public void testChangeUsername() {
        User testUser = userService.getUserByUserName(testUserName);
        testUser = userService.changeUserName(testUser, testUserName2);
        Assert.assertEquals(testUser.getUsername(), testUserName2);
        testUser = userService.getUserByUserName(testUserName2);
        Assert.assertEquals(testUser.getUsername(), testUserName2);
        testUser = userService.changeUserName(testUser, testUserName);
    }


    @Test
    public void testUserDelete() {
            User testUser = userService.getUserByUserName(testUserName);

           //Delete the user
           userService.deleteUser(testUser);

           //Check to make sure the user has been deleted
           User user = userService.getUserByUserName(testUser.getUsername());
           Assert.assertNull(user);

       }

}
