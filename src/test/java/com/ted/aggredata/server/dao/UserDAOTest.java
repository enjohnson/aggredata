package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.User;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        user.setPassword(password);
        user.setActivationKey(activationKey);
        user.setDefaultGroupId(1);
        user.setRole("ROLE_USER");
        user.setState(true);
        
        //Test create
        User createdUser = userDAO.create(user);
        Assert.assertNotNull(createdUser);
        Assert.assertEquals(createdUser.getUsername(), username);
        Assert.assertEquals(createdUser.getPassword(), password);
        Assert.assertEquals(createdUser.getActivationKey(), activationKey);
        Assert.assertEquals(createdUser.getRole(), "ROLE_USER");
        Assert.assertEquals(createdUser.isState(), true);
        
        //Test Save
        String password2 = TestUtil.getUniqueKey();
        String activationKey2 = TestUtil.getUniqueKey();

        createdUser.setPassword(password2);
        createdUser.setActivationKey(activationKey2);
        createdUser.setState(false);
        createdUser.setRole("ROLE_NONE");

        userDAO.save(createdUser);

        //Save test and getUserByUserName test
        User savedUser = userDAO.getUserByUserName(username);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(savedUser.getUsername(), username);
        Assert.assertEquals(savedUser.getPassword(), password2);
        Assert.assertEquals(savedUser.getActivationKey(), activationKey2);
        Assert.assertEquals(savedUser.getRole(), "ROLE_NONE");
        Assert.assertEquals(savedUser.isState(), false);


        userDAO.delete(savedUser);
        Assert.assertNull(userDAO.findById(savedUser.getId()));



    }


}
