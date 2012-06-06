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

import com.ted.aggredata.server.model.EnergyPostRecord;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Unit tests for the energy post service
 */
public class EnergyPostServiceTest {

    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();


    @Test
    public void testFullEnergyPostRecord() {
       //Test the XML parsing
       //Test full record
        String record = "<ted5000 GWID=\"210007\" auth=\"myToken\">" +
                            "<COST mrd=\"15\" fixed=\"10.00\" min=\"5.00\"/>" +
                            "<DEMAND kVA=\"0\"><demandCost timestamp=\"1338398580\" peak=\"12.916\" cost=\"23.83\"/></DEMAND>" +
                            "<MTU ID=\"222222\" type=\"0\">" +
                            "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                            "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                            "</MTU>" +
                            "<MTU ID=\"222223\" type=\"1\">" +
                            "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                            "</MTU>" +
                            "</ted5000>";

        try {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

            InputStream is = new ByteArrayInputStream(record.getBytes("UTF-8"));
            Document doc = db.parse(is);
            EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);

            Assert.assertEquals(energyPostRecord.getGatewayId(), "210007");
            Assert.assertEquals((int)energyPostRecord.getCostRecord().getMeterReadDay(), 15);
            Assert.assertEquals((double)energyPostRecord.getCostRecord().getFixedCharge(), 10.00, 0.01);
            Assert.assertEquals((double)energyPostRecord.getCostRecord().getMinCharge(), 5.00, 0.01);

            Assert.assertEquals((boolean)energyPostRecord.getDemandKVA(), false);
            Assert.assertEquals(energyPostRecord.getDemandRecords().size(), 1);
            Assert.assertEquals((int)energyPostRecord.getDemandRecords().get(0).getTimestamp(),1338398580);
            Assert.assertEquals((double)energyPostRecord.getDemandRecords().get(0).getPeak(),12.916, .001);
            Assert.assertEquals((double)energyPostRecord.getDemandRecords().get(0).getCost(),23.83, .01);

            Assert.assertEquals(energyPostRecord.getMtuRecords().size(), 2);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getId(), Long.parseLong("222222", 16));
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getId(), Long.parseLong("222223", 16));
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getType(), 0);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getType(), 1);

            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().size(), 2);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getCumulativeCostRecordList().size(), 1);

            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getTimestamp(), 1338398520);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getTimestamp(), 1338398580);

            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getEnergy(), 10000, 1.0);
            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getEnergy(), 2000, 1.0);

            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getRate(), 1, 1.0);
            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getRate(), 1, 1.0);

        } catch (Exception ex) {
            Assert.fail("Exception caught: " +ex.getMessage());
        }

    }



    @Test
    public void testOldEnergyRecord() {
        //Test the XML parsing
        //Test full record
        String record = "<ted5000 GWID=\"210007\" auth=\"myToken\">" +
                "<MTU ID=\"222222\" type=\"0\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                "<cumulative timestamp=\"1338398580\" watts=\"2000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "<MTU ID=\"222223\" type=\"1\">" +
                "<cumulative timestamp=\"1338398520\" watts=\"10000\" rate=\"1.00000\"/>" +
                "</MTU>" +
                "</ted5000>";

        try {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

            InputStream is = new ByteArrayInputStream(record.getBytes("UTF-8"));
            Document doc = db.parse(is);
            EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);

            Assert.assertEquals(energyPostRecord.getGatewayId(), "210007");

            Assert.assertEquals((int)energyPostRecord.getCostRecord().getMeterReadDay(), 1);
            Assert.assertEquals((double)energyPostRecord.getCostRecord().getFixedCharge(), 0, 0.01);
            Assert.assertEquals((double)energyPostRecord.getCostRecord().getMinCharge(), 0, 0.01);

            Assert.assertEquals((boolean)energyPostRecord.getDemandKVA(), false);
            Assert.assertEquals(energyPostRecord.getDemandRecords().size(), 0);

            Assert.assertEquals(energyPostRecord.getMtuRecords().size(), 2);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getId(), Long.parseLong("222222", 16));
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getId(), Long.parseLong("222223", 16));
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getType(), 0);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getType(), 1);

            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().size(), 2);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(1).getCumulativeCostRecordList().size(), 1);

            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getTimestamp(), 1338398520);
            Assert.assertEquals((long)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getTimestamp(), 1338398580);

            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getEnergy(), 10000, 1.0);
            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getEnergy(), 2000, 1.0);

            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(0).getRate(), 1, 1.0);
            Assert.assertEquals((double)energyPostRecord.getMtuRecords().get(0).getCumulativeCostRecordList().get(1).getRate(), 1, 1.0);

        } catch (Exception ex) {
            Assert.fail("Exception caught: " +ex.getMessage());
        }

    }
}
