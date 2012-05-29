package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.EnergyData;
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

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml"
})
public class EnergyDataDAOTest {



    
    @Autowired
    GroupDAO groupDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    GatewayDAO gatewayDAO;

    @Autowired
    MTUDAO mtuDAO;

    @Autowired
    EnergyDataDAO energyDataDAO;



    @Autowired
    EnergyDataHistoryDAO energyDataHistoryDAO;

    User gatewayOwner1 = null;


    @Test
    public void testEnergyData() {

        Gateway oldGateway = gatewayDAO.findById(GatewayDAOTest.GATEWAY_TEST_ID);
        if (oldGateway != null) gatewayDAO.delete(oldGateway);

        
        //Set up data
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
        mtu.setId(MTUDAOTest.MTU_TEST_ID);
        mtu.setGatewayId(gateway.getId());
        mtu.setDescription(TestUtil.getUniqueKey());
        mtu.setType(MTU.MTUType.LOAD);
        mtu = mtuDAO.create(gateway, mtu);

        Date testDate = new Date(10001);

        //Create 10 rows of data
        for (int i=0; i < 10; i++)
        {
            EnergyData energyData = new EnergyData();
            energyData.setGatewayId(gateway.getId());
            energyData.setMtuId(mtu.getId());
            energyData.setEnergy(10000d);
            energyData.setRate(1.0000d);
            energyData.setTimestamp(10000 + i);
            energyData.setMeterReadDay(1);
            energyData.setMeterReadMonth(testDate.getMonth());
            energyData.setMeterReadYear(testDate.getYear());
            energyDataDAO.create(energyData);
        }


        List<EnergyData> energyDataList = energyDataDAO.findByDateRange(gateway, mtu, 10001, 10005);
        Assert.assertEquals(energyDataList.size(), 4);
        
        for (int i=0; i < 4; i++)
        {
            Assert.assertEquals(new Integer(10001+i), energyDataList.get(i).getTimestamp());
            
        }

        energyDataDAO.deleteEnergyData(gateway, mtu);
        energyDataList = energyDataDAO.findByDateRange(gateway, mtu, 10001, 10005);

        energyDataHistoryDAO.findMonthHistory(gateway, mtu, 10001, 10005, "EST", "EST");
        energyDataHistoryDAO.findDailyHistory(gateway, mtu, 10001, 10005, "EST", "EST");
        energyDataHistoryDAO.findHourlyHistory(gateway, mtu, 10001, 10005, "EST", "EST");



        Assert.assertEquals(0, energyDataList.size());




        mtuDAO.delete(gateway, mtu);
        gatewayDAO.delete(gateway);
        userDAO.delete(user);
        
        

    }
}
