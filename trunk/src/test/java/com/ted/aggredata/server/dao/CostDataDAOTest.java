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

import com.ted.aggredata.model.*;
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
public class CostDataDAOTest {


    @Autowired
    CostDataDAO costDataDAO;


    @Test
    public void testCostData() {
        Gateway gateway = new Gateway();
        gateway.setId(12345l);
        //Clear out old data
        costDataDAO.deleteEnergyData(gateway);

        //Create cost data
        CostData testCostData = new CostData();
        testCostData.setGatewayId(12345l);
        testCostData.setTimestamp(1000);
        testCostData.setMeterReadDay(1);
        testCostData.setMeterReadMonth(2);
        testCostData.setMeterReadYear(2012);
        costDataDAO.create(testCostData);

        //Make sure it exists
        CostData savedCostData = costDataDAO.findByTimestamp(12345l, 1000);
        Assert.assertNotNull(savedCostData);
        Assert.assertEquals((long)savedCostData.getGatewayId(), 12345l);
        Assert.assertEquals((long)savedCostData.getTimestamp(), 1000);
        Assert.assertEquals((long)savedCostData.getMeterReadDay(), 1);
        Assert.assertEquals((long)savedCostData.getMeterReadMonth(), 2);
        Assert.assertEquals((long)savedCostData.getMeterReadYear(), 2012);

        //Test update
        testCostData.setMeterReadDay(15);
        costDataDAO.create(testCostData);


        //Make sure it is update
        savedCostData = costDataDAO.findByTimestamp(12345l, 1000);
        Assert.assertNotNull(savedCostData);
        Assert.assertEquals((long)savedCostData.getGatewayId(), 12345l);
        Assert.assertEquals((long)savedCostData.getTimestamp(), 1000);
        Assert.assertEquals((long)savedCostData.getMeterReadDay(), 15);
        Assert.assertEquals((long)savedCostData.getMeterReadMonth(), 2);
        Assert.assertEquals((long)savedCostData.getMeterReadYear(), 2012);


        //Clear out data
        costDataDAO.deleteEnergyData(gateway);


        savedCostData = costDataDAO.findByTimestamp(12345l, 1000);
        Assert.assertNull(savedCostData);

    }
}
