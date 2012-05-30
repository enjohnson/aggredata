package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.User;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.zip.CheckedInputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml"
})
public class UserDAOTest {

    @Autowired
    protected UserDAO userDAO;

    @Test
    public void testUserDAO()
    {
        String username = TestUtil.getUniqueKey();
        String password = TestUtil.getUniqueKey();
        String activationKey = TestUtil.getUniqueKey();
        
        User user = new User();
        user.setUsername(username);
        user.setActivationKey(activationKey);
        user.setDefaultGroupId(1);
        user.setRole("ROLE_USER");
        user.setAccountState(User.STATE_ENABLED);
        
        //Test create
        User createdUser = userDAO.create(user);
        Assert.assertNotNull(createdUser);
        Assert.assertEquals(createdUser.getUsername(), username);
        Assert.assertEquals(createdUser.getActivationKey(), activationKey);
        Assert.assertEquals(createdUser.getRole(), "ROLE_USER");
        Assert.assertEquals(createdUser.getAccountState(), User.STATE_ENABLED);
        
        //Test Save
        String password2 = TestUtil.getUniqueKey();
        String activationKey2 = TestUtil.getUniqueKey();

        createdUser.setActivationKey(activationKey2);
        createdUser.setAccountState(User.STATE_DISABLED);
        createdUser.setRole("ROLE_NONE");
        userDAO.save(createdUser);

        //Save test and getUserByUserName test
        User savedUser = userDAO.getUserByUserName(username);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(savedUser.getUsername(), username);
        Assert.assertEquals(savedUser.getActivationKey(), activationKey2);
        Assert.assertEquals(savedUser.getRole(), "ROLE_NONE");
        Assert.assertEquals(savedUser.getAccountState(), User.STATE_DISABLED);

        savedUser = userDAO.getUserByKey("nokey");
        Assert.assertNull(savedUser);

        savedUser = userDAO.getUserByKey(activationKey2);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(savedUser.getUsername(), username);
        Assert.assertEquals(savedUser.getActivationKey(), activationKey2);
        Assert.assertEquals(savedUser.getRole(), "ROLE_NONE");
        Assert.assertEquals(savedUser.getAccountState(), User.STATE_DISABLED);


        userDAO.delete(savedUser);
        Assert.assertNull(userDAO.findById(savedUser.getId()));



    }

    @Test
    public void testFindUsers(){

        ArrayList <User> userList = new ArrayList<User>();

        //Create sample users
        for (int i=0; i < 10; i++) {
            User user = new User();
            user.setUsername(TestUtil.getUniqueKey() + "@theenergydetective.com");
            user.setActivationKey("12345");
            user.setDefaultGroupId(1);
            user.setRole("ROLE_USER");
            user.setFirstName("USER" + i);


            if (i < 5) {
                user.setLastName("LNAME" + i);
                user.setCompanyName("Company A");
            }  else {
                user.setLastName("LAST" + i);
                user.setCompanyName("Company B");
            }
            user.setAccountState(User.STATE_ENABLED);
            userList.add(userDAO.create(user));
        }


        //Check the various queries
        if (userDAO.findUsers().size() < 10) Assert.fail("Not enough users returned");

        int allSize = userDAO.findUsers("@theenergydetective").size();
        int compASize = userDAO.findUsers("Company A").size();
        int compBSize = userDAO.findUsers("Company B").size();


        //Clean up
        for (User user: userList) {
            userDAO.delete(user);
        }

        if (allSize < 10) Assert.fail("Not enough @energydetective users returned:" + allSize);
        if (compASize != 5) Assert.fail("Not enough Company A users returned:" + compASize);
        if (compBSize != 5) Assert.fail("Not enough Company B users returned:" + compBSize);


    }



}
