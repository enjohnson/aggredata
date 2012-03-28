package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.util.TestUtil;
import org.junit.Assert;
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
public class GatewayDAOTest {

    public static final Long GATEWAY_TEST_ID = 1l;
    
    @Autowired
    GroupDAO groupDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    GatewayDAO gatewayDAO;

    
    User gatewayOwner1 = null;


    @Test
    public void testGatewayDAO()
    {
        /**
         * Set up test case
         */
        //Clean up old test data
        Gateway oldGateway = gatewayDAO.findById(GATEWAY_TEST_ID);
        if (oldGateway != null) gatewayDAO.delete(oldGateway);

        gatewayOwner1 = new User();
        gatewayOwner1.setUsername(TestUtil.getUniqueKey());
        gatewayOwner1.setActivationKey(TestUtil.getUniqueKey());
        gatewayOwner1.setRole("ROLE_USER");
        gatewayOwner1.setState(true);

        gatewayOwner1  = userDAO.create(gatewayOwner1);
        Assert.assertNotNull(gatewayOwner1);


        Gateway gateway = new Gateway();
        gateway.setDescription(TestUtil.getUniqueKey());
        gateway.setId(GATEWAY_TEST_ID);
        gateway.setState(true);
        gateway.setUserAccountId(gatewayOwner1.getId());

        try
        {
            gateway = gatewayDAO.create(gateway);
        } catch (GatewayExistsException ex)
        {
            Assert.fail("Gateway Exists already");
        }

        Gateway gateway2 = gatewayDAO.findById(GATEWAY_TEST_ID);
        List<Gateway> gateway3 = gatewayDAO.findByUserAccount(gatewayOwner1);
        
        Assert.assertEquals(gateway.getId(), gateway2.getId());
        Assert.assertEquals(gateway.getId(), gateway3.get(0).getId());
        
        //Group Test

        Group group = groupDAO.create(gatewayOwner1, TestUtil.getUniqueKey());
        gatewayDAO.addGatewayToGroup(gateway, group);
        Assert.assertEquals(gatewayDAO.findByGroup(group).size(), 1);

        gatewayDAO.removeGatewayFromGroup(gateway,group);
        Assert.assertEquals(gatewayDAO.findByGroup(group).size(), 0);


        /**
         * Clean up test case
         */

        groupDAO.delete(group);
        gatewayDAO.delete(gateway);
        Assert.assertNull(gatewayDAO.findById(GATEWAY_TEST_ID));
        Assert.assertEquals(gatewayDAO.findByUserAccount(gatewayOwner1).size(), 0);

        userDAO.delete(gatewayOwner1);


    }
}
