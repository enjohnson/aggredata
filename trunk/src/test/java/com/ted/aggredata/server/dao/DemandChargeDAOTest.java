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

package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.DemandCharge;
import com.ted.aggredata.model.Gateway;
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
public class DemandChargeDAOTest {


    @Autowired
    DemandChargeDAO demandChargeDAO;


    @Test
    public void testDemandChargeData() {
        Gateway gateway = new Gateway();
        gateway.setId(12345l);
        //Clear out old data
        demandChargeDAO.deleteEnergyData(gateway);

        //Create data
        DemandCharge testDemandCharge = new DemandCharge();
        testDemandCharge.setGatewayId(12345l);
        testDemandCharge.setTimestamp(1000);
        testDemandCharge.setCost(10.00);
        testDemandCharge.setPeak(20.00);
        demandChargeDAO.create(testDemandCharge);

        //Make sure it exists
        DemandCharge savedDemandCharge = demandChargeDAO.findByTimestamp(12345l, 1000);
        Assert.assertNotNull(savedDemandCharge);
        Assert.assertEquals((long) savedDemandCharge.getGatewayId(), 12345l);
        Assert.assertEquals((long)savedDemandCharge.getTimestamp(), 1000);
        Assert.assertEquals(savedDemandCharge.getCost(), 10.00, .01);
        Assert.assertEquals(savedDemandCharge.getPeak(), 20.00, .01);

        //Test update
        testDemandCharge.setCost(15.00);
        demandChargeDAO.create(testDemandCharge);


        //Make sure it is update
        savedDemandCharge = demandChargeDAO.findByTimestamp(12345l, 1000);
        Assert.assertNotNull(savedDemandCharge);
        Assert.assertEquals((long)savedDemandCharge.getGatewayId(), 12345l);
        Assert.assertEquals((long) savedDemandCharge.getTimestamp(), 1000);
        Assert.assertEquals(savedDemandCharge.getCost(), 15.00, .01);
        Assert.assertEquals(savedDemandCharge.getPeak(), 20.00, .01);



        //Clear out data
        demandChargeDAO.deleteEnergyData(gateway);


        savedDemandCharge = demandChargeDAO.findByTimestamp(12345l, 1000);
        Assert.assertNull(savedDemandCharge);

    }
}
