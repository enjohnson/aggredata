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

import com.ted.aggredata.model.*;
import com.ted.aggredata.server.dao.*;
import com.ted.aggredata.server.model.DemandRecord;
import com.ted.aggredata.server.model.EnergyPostRecord;
import com.ted.aggredata.server.services.impl.EnergyPostServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Unit tests for the energy post service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-Test.xml"
})
public class EnergyPostServiceTest {

    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Autowired
    EnergyPostService energyPostService;

    @Autowired
    GatewayDAO gatewayDAO;

    @Autowired
    MTUDAO mtuDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    DemandChargeDAO demandChargeDAO;

    @Autowired
    CostDataDAO costDataDAO;

    @Autowired
    EnergyDataDAO energyDataDAO;



    @Test
    public void testDemandCharge() throws Exception{
        String record = "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                "<DEMAND kVA=\"0\">" +
                "<demandCost timestamp=\"1338398520\" peak=\"12.916\" cost=\"23.83\"/>" +
                "<demandCost timestamp=\"1338398580\" peak=\"12.916\" cost=\"23.83\"/>" +
                "</DEMAND>" +
                "<MTU ID=\"211555\" type=\"0\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "<MTU ID=\"211556\" type=\"1\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "</ted5000>";


        EnergyPostServiceImpl energyPostServiceImpl = (EnergyPostServiceImpl) energyPostService;

        //Create the test user
        User user = userDAO.getUserByUserName("energyPostService@theenergydetective.com");
        if (user == null) {
            user = new User();
            user.setUsername("energyPostService@theenergydetective.com");
            user.setTimezone("US/Eastern");
            user.setAccountState(User.STATE_ENABLED);
            user = userDAO.create(user);
        }

        //Create a test gateway
        Gateway testGateway = new Gateway();
        testGateway.setId(Long.parseLong("211005", 16));
        testGateway.setSecurityKey("ABCDEFG");
        testGateway.setUserAccountId(user.getId());
        gatewayDAO.delete(testGateway);
        testGateway = gatewayDAO.create(testGateway);


        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(record.getBytes("UTF-8"));
        Document doc = db.parse(is);
        EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);

        for (DemandRecord demandRecord: energyPostRecord.getDemandRecords()) {
            energyPostServiceImpl.postDemandCharge(testGateway, energyPostRecord.getDemandKVA()?1:0, demandRecord );
        }


        DemandCharge demandCharge  = demandChargeDAO.findByTimestamp(testGateway.getId(),  1338398520);
        Assert.assertNotNull(demandCharge);
        Assert.assertEquals(demandCharge.getType(), 0);
        Assert.assertEquals((long)demandCharge.getTimestamp(), 1338398520);
        Assert.assertEquals(demandCharge.getPeak(), 12.916, .001);

        demandCharge  = demandChargeDAO.findByTimestamp(testGateway.getId(),  1338398580);
        Assert.assertNotNull(demandCharge);
        Assert.assertEquals(demandCharge.getType(), 0);
        Assert.assertEquals((long)demandCharge.getTimestamp(), 1338398580);
        Assert.assertEquals(demandCharge.getPeak(), 12.916, .001);


        //clean up
        userDAO.delete(user);

    }


    @Test
    public void testNewEnergyPost() throws Exception{
        String record = "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                "<DEMAND kVA=\"0\">" +
                "<demandCost timestamp=\"1338398520\" peak=\"12.916\" cost=\"23.83\"/>" +
                "<demandCost timestamp=\"1338398580\" peak=\"12.916\" cost=\"23.83\"/>" +
                "</DEMAND>" +
                "<MTU ID=\"211555\" type=\"0\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"1000\" rate=\"1.00000\"/>" +
                "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                "<cumulative timestamp=\"1338398640\" watts=\"3000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "<MTU ID=\"211556\" type=\"1\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "</ted5000>";



        EnergyPostServiceImpl energyPostServiceImpl = (EnergyPostServiceImpl) energyPostService;

        //Create the test user
        User user = userDAO.getUserByUserName("energyPostService@theenergydetective.com");
        if (user == null) {
            user = new User();
            user.setUsername("energyPostService@theenergydetective.com");
            user.setTimezone("US/Eastern");
            user.setAccountState(User.STATE_ENABLED);
            user = userDAO.create(user);
        }

        //Create a test gateway
        Gateway testGateway = new Gateway();
        testGateway.setId(Long.parseLong("211005", 16));
        testGateway.setSecurityKey("ABCDEFG");
        testGateway.setUserAccountId(user.getId());
        gatewayDAO.delete(testGateway);
        testGateway = gatewayDAO.create(testGateway);


        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(record.getBytes("UTF-8"));
        Document doc = db.parse(is);
        EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);

        energyPostService.postEnergyData(user, testGateway, energyPostRecord);

        CostData costData = costDataDAO.findByTimestamp(testGateway.getId(), 1338398520);
        Assert.assertNotNull(costData);
        Assert.assertEquals(costData.getFixedCost(), 10.00, .01);
        Assert.assertEquals(costData.getMinCost(), 5.00, .01);
        Assert.assertEquals((long)costData.getTimestamp(), 1338398520);
        Assert.assertEquals((int)costData.getMeterReadDay(), 15);

        costData = costDataDAO.findByTimestamp(testGateway.getId(), 1338398580);
        Assert.assertNotNull(costData);
        Assert.assertEquals(costData.getFixedCost(), 10.00, .01);
        Assert.assertEquals(costData.getMinCost(), 5.00, .01);
        Assert.assertEquals((long)costData.getTimestamp(), 1338398580);
        Assert.assertEquals((int)costData.getMeterReadDay(), 15);


        costData = costDataDAO.findByTimestamp(testGateway.getId(), 1338398640);
        Assert.assertNotNull(costData);
        Assert.assertEquals(costData.getFixedCost(), 10.00, .01);
        Assert.assertEquals(costData.getMinCost(), 5.00, .01);
        Assert.assertEquals((long)costData.getTimestamp(), 1338398640);
        Assert.assertEquals((int)costData.getMeterReadDay(), 15);



        DemandCharge demandCharge  = demandChargeDAO.findByTimestamp(testGateway.getId(),  1338398520);
        Assert.assertNotNull(demandCharge);
        Assert.assertEquals(demandCharge.getType(), 0);
        Assert.assertEquals((long)demandCharge.getTimestamp(), 1338398520);
        Assert.assertEquals(demandCharge.getPeak(), 12.916, .001);

        demandCharge  = demandChargeDAO.findByTimestamp(testGateway.getId(),  1338398580);
        Assert.assertNotNull(demandCharge);
        Assert.assertEquals(demandCharge.getType(), 0);
        Assert.assertEquals((long)demandCharge.getTimestamp(), 1338398580);
        Assert.assertEquals(demandCharge.getPeak(), 12.916, .001);


        MTU mtu1 = mtuDAO.findById(testGateway.getId(), Long.parseLong("211555", 16));
        MTU mtu2 = mtuDAO.findById(testGateway.getId(), Long.parseLong("211556", 16));

        List<EnergyData> energyDataList1= energyDataDAO.findByDateRange(testGateway, mtu1, 1338390000, 1338400000);
        Assert.assertEquals(energyDataList1.size(), 3);
        Assert.assertEquals((int)energyDataList1.get(0).getTimestamp(), 1338398520);
        Assert.assertEquals((int)energyDataList1.get(1).getTimestamp(), 1338398580);
        Assert.assertEquals((int)energyDataList1.get(2).getTimestamp(), 1338398640);

        Assert.assertEquals(energyDataList1.get(0).getEnergyDifference(), 0, .0001);
        Assert.assertEquals(energyDataList1.get(1).getEnergyDifference(), 1000, .0001);
        Assert.assertEquals(energyDataList1.get(2).getEnergyDifference(), 1000, .0001);



        List<EnergyData> energyDataList2= energyDataDAO.findByDateRange(testGateway, mtu2, 1338390000, 1338400000);
        Assert.assertEquals(energyDataList2.size(), 1);




        //clean up
        userDAO.delete(user);

    }



    @Test
    public void testHistoricalEnergyPost() throws Exception{
        String records[] = {
                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398520\" watts=\"1000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>",

                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>",

                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398640\" watts=\"3000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>"};



        EnergyPostServiceImpl energyPostServiceImpl = (EnergyPostServiceImpl) energyPostService;

        //Create the test user
        User user = userDAO.getUserByUserName("energyPostService@theenergydetective.com");
        if (user == null) {
            user = new User();
            user.setUsername("energyPostService@theenergydetective.com");
            user.setTimezone("US/Eastern");
            user.setAccountState(User.STATE_ENABLED);
            user = userDAO.create(user);
        }

        //Create a test gateway
        Gateway testGateway = new Gateway();
        testGateway.setId(Long.parseLong("211005", 16));
        testGateway.setSecurityKey("ABCDEFG");
        testGateway.setUserAccountId(user.getId());
        gatewayDAO.delete(testGateway);
        testGateway = gatewayDAO.create(testGateway);


        for (int i=0; i < records.length; i++) {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(records[i].getBytes("UTF-8"));
            Document doc = db.parse(is);
            EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);
            is.close();
            energyPostService.postEnergyData(user, testGateway, energyPostRecord);
        }


        MTU mtu1 = mtuDAO.findById(testGateway.getId(), Long.parseLong("211555", 16));


        List<EnergyData> energyDataList1= energyDataDAO.findByDateRange(testGateway, mtu1, 1338390000, 1338400000);
        Assert.assertEquals(energyDataList1.size(), 3);


        for (int i=0; i < 3; i++) {
            int timestamp = 1338398520 + (i*60);


            Assert.assertEquals((int)energyDataList1.get(i).getTimestamp(), timestamp);


            CostData costData = costDataDAO.findByTimestamp(testGateway.getId(), timestamp);
            Assert.assertNotNull(costData);
            Assert.assertEquals(costData.getFixedCost(), 10.00, .01);
            Assert.assertEquals(costData.getMinCost(), 5.00, .01);
            Assert.assertEquals((long)costData.getTimestamp(), timestamp);
            Assert.assertEquals((int)costData.getMeterReadDay(), 15);
        }

        Assert.assertEquals(energyDataList1.get(0).getEnergyDifference(), 0, .0001);
        for (int i=1; i < 3; i++) {
            Assert.assertEquals(energyDataList1.get(i).getEnergyDifference(), 1000, .0001);
            Assert.assertEquals(energyDataList1.get(i).getMinuteCost(), 1, .0001);
        }


        //clean up
        userDAO.delete(user);

    }



    @Test
    public void testSkipEnergyPost() throws Exception{
        String records[] = {
                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398520\" watts=\"1000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>",

                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>",

                "<ted5000 GWID=\"211005\" auth=\"myToken\">" +
                        "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                        "<MTU ID=\"211555\" type=\"0\">" +
                        "<cumulative timestamp=\"1338398880\" watts=\"7000\" rate=\"1.00000\"/>" +
                        "</MTU>" +
                        "</ted5000>"};



        EnergyPostServiceImpl energyPostServiceImpl = (EnergyPostServiceImpl) energyPostService;

        //Create the test user
        User user = userDAO.getUserByUserName("energyPostService@theenergydetective.com");
        if (user == null) {
            user = new User();
            user.setUsername("energyPostService@theenergydetective.com");
            user.setTimezone("US/Eastern");
            user.setAccountState(User.STATE_ENABLED);
            user = userDAO.create(user);
        }

        //Create a test gateway
        Gateway testGateway = new Gateway();
        testGateway.setId(Long.parseLong("211005", 16));
        testGateway.setSecurityKey("ABCDEFG");
        testGateway.setUserAccountId(user.getId());
        gatewayDAO.delete(testGateway);
        testGateway = gatewayDAO.create(testGateway);


        for (int i=0; i < records.length; i++) {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(records[i].getBytes("UTF-8"));
            Document doc = db.parse(is);
            EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);
            is.close();
            energyPostService.postEnergyData(user, testGateway, energyPostRecord);
        }


        MTU mtu1 = mtuDAO.findById(testGateway.getId(), Long.parseLong("211555", 16));


        List<EnergyData> energyDataList1= energyDataDAO.findByDateRange(testGateway, mtu1, 1338390000, 1338400000);
        Assert.assertEquals(energyDataList1.size(), 7);


        for (int i=0; i < 7; i++) {
            int timestamp = 1338398520 + (i*60);


            Assert.assertEquals((int)energyDataList1.get(i).getTimestamp(), timestamp);


            CostData costData = costDataDAO.findByTimestamp(testGateway.getId(), timestamp);
            Assert.assertNotNull(costData);
            Assert.assertEquals(costData.getFixedCost(), 10.00, .01);
            Assert.assertEquals(costData.getMinCost(), 5.00, .01);
            Assert.assertEquals((long)costData.getTimestamp(), timestamp);
            Assert.assertEquals((int)costData.getMeterReadDay(), 15);
        }

        Assert.assertEquals(energyDataList1.get(0).getEnergyDifference(), 0, .0001);
        for (int i=1; i < 7; i++) {
            Assert.assertEquals(energyDataList1.get(i).getEnergyDifference(), 1000, .0001);
            Assert.assertEquals(energyDataList1.get(i).getMinuteCost(), 1, .0001);
        }


        //clean up
        userDAO.delete(user);

    }

}
