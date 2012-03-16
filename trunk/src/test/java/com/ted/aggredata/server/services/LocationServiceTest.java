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

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.LocationService;
import com.ted.aggredata.server.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
                                   "classpath:applicationContext-Test.xml"
                    })

public class LocationServiceTest
{
    @Autowired
    protected UserService userService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected LocationService locationService;


    public static User testUser;
    public static Group testGroup;
    public static Location testLocation;


    @Before
     public void setUp() throws Exception {
        testUser = new User();
        testUser.setUsername("grouptestuser@theenergydetective.com");
        testUser.setPassword("aggredata");
        testUser.setDefaultGroupId(0);
        testUser.setRole(User.Role.MEMBER);
        testUser.setState(true);
        userService.createUser(testUser);
        testUser = userService.getUserByUserName("grouptestuser@theenergydetective.com");

        groupService.createGroup(testUser, "Test Group");
        testGroup = groupService.getByUser(testUser).get(0);

        testLocation = new Location();
        testLocation.setUserId(testUser.getId());
        testLocation.setAddress1("1 Address Way");
        testLocation.setAddress2("");
        testLocation.setCity("Charleston");
        testLocation.setStateOrProvince("South Carolina");
        testLocation.setPostal("29401");
        testLocation.setState(true);
        testLocation.setCountry("USA");
        testLocation.setDescription("Test Location");

        locationService.addLocation(testUser, testLocation);
        testLocation = locationService.getLocation(testUser, testLocation.getDescription());
     }

     @After
     public void tearDown() throws Exception {
         locationService.removeLocation(testLocation);
         groupService.deleteGroup(testGroup);
         userService.deleteUser(testUser);
     }

    @Test
    public void testFindByUser()
    {
        List<Location> locationList =  locationService.getLocations(testUser);
        Assert.assertTrue(locationList.contains(testLocation));
    }


    @Test
    public void testAddLocationToGroup()
    {
        locationService.addLocationToGroup(testLocation, testGroup);
        List<Location> locationList =  locationService.getLocations(testGroup);
        Assert.assertTrue(locationList.contains(testLocation));
    }

}