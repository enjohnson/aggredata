package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;
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
public class MTUDAOTest {

    public static final Long MTU_TEST_ID = 1l;

    
    @Autowired
    GroupDAO groupDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    GatewayDAO gatewayDAO;

    @Autowired
    MTUDAO mtuDAO;


    User gatewayOwner1 = null;


    @Test
    public void testMTUDAO() {

        //Clean up old test data
        Gateway oldGateway = gatewayDAO.findById(1l);
        if (oldGateway != null) gatewayDAO.delete(oldGateway);


        User user = new User();
        user.setUsername(TestUtil.getUniqueKey());
        user = userDAO.create(user);

        Gateway gateway = new Gateway();
        gateway.setId(GatewayDAOTest.GATEWAY_TEST_ID);
        gateway.setUserAccountId(user.getId());
        gateway.setDescription(TestUtil.getUniqueKey());
        gateway.setState(true);
        try {
            gateway = gatewayDAO.create(gateway);
        } catch (Exception ex) {
            Assert.fail("Gateway Exists already");
        }
        
        MTU mtu = new MTU();
        mtu.setId(MTU_TEST_ID);
        mtu.setGatewayId(gateway.getId());
        mtu.setDescription(TestUtil.getUniqueKey());
        mtu.setType(MTU.MTUType.LOAD);

        mtu = mtuDAO.create(gateway, mtu);
        Assert.assertNotNull(mtu.getId());
        
        
        MTU mtu2 = mtuDAO.findById(gateway.getId(), MTU_TEST_ID);
        Assert.assertEquals(mtu2.getId(), mtu.getId());
        Assert.assertEquals(mtuDAO.findByGateway(gateway).size(), 1);
        
        MTU mtu3 = mtuDAO.findById(gateway.getId(), MTU_TEST_ID);
        mtu3.setDescription(TestUtil.getUniqueKey());
        mtu3.setType(MTU.MTUType.STAND_ALONE);
        mtuDAO.save(mtu3);
        
        MTU mtu4 = mtuDAO.findById(gateway.getId(), MTU_TEST_ID);
        Assert.assertEquals(mtu3.getId(), mtu4.getId());
        Assert.assertEquals(mtu3.getDescription(), mtu4.getDescription());
        Assert.assertEquals(mtu3.getType(), mtu4.getType());

        mtuDAO.delete(gateway, mtu4);
        Assert.assertNull(mtuDAO.findById(gateway.getId(), MTU_TEST_ID));

        gatewayDAO.delete(gateway);
        userDAO.delete(user);


    }
}
