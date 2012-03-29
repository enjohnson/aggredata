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

import com.ted.aggredata.model.User;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User Service Unit test
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml",
        "classpath:security-app-context.xml"
        
})



public class UserServiceTest {

    @Autowired
    AuthenticationManager authenticationManager;


    Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    static String testUserName = TestUtil.getUniqueKey();
    static String testUserName2 = TestUtil.getUniqueKey();


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
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.ROLE_USER);
        testUser.setState(true);
        testUser.setActivationKey(TestUtil.getUniqueKey());

        //Save the user
        userService.createUser(testUser);

        //Load the user
        testUser = userService.getUserByUserName(testUser.getUsername());
        Assert.assertNotNull(testUser);

        testUser = userService.getUserByActivationKey(testUser.getActivationKey());
        Assert.assertNotNull(testUser);

    }

    @Test
    public void testChangePassword() {
        User testUser = userService.getUserByUserName(testUserName);
        testUser = userService.changePassword(testUser, "password2");

        try {
            Authentication request = new UsernamePasswordAuthenticationToken(testUserName, "password2");
            if (authenticationManager == null) logger.error("AuthenticationManager is null");
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            Assert.fail("Authentication failed:" + e.getMessage());
        }

        testUser = userService.changePassword(testUser, "password3");
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(testUserName, "password3");
            if (authenticationManager == null) logger.error("AuthenticationManager is null");
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            Assert.fail("Authentication failed:" + e.getMessage());
        }


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
