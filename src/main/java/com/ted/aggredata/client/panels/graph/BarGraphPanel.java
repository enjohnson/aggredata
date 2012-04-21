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

package com.ted.aggredata.client.panels.graph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.model.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Superclass for the bar graph panels
 */
public abstract class BarGraphPanel extends Composite implements GraphOptionChangeable {

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);
    static Logger logger = Logger.getLogger(BarGraphPanel.class.toString());
    protected Group group;
    protected Date startDate;
    protected Date endDate;
    protected Enums.GraphType graphType;
    protected EnergyDataHistoryQueryResult historyResult = null;
    protected VerticalPanel barGraphPanel = new VerticalPanel();

    /**
     * Callback to handle the loading of history data.
     */
    Runnable onLoadCallback = new Runnable() {
        public void run() {
            if (historyResult != null)
            {
                //Create the visualization
                barGraphPanel.clear();
                BarChart barChart = new BarChart(createTable(), createOptions());
                barGraphPanel.add(barChart);
            }
        }
    };


    public void onGraphOptionChange(Group group, Date startDate, Date endDate, Enums.GraphType graphType) {

        this.group = group;
        this.startDate = fixStartDate(startDate);
        this.endDate = fixEndDate(endDate);

        if (logger.isLoggable(Level.FINE)) logger.fine("Graph Option Change Called: " + group + " " + this.startDate + " " + this.endDate + " " + graphType);


        this.graphType = graphType;


        groupService.getHistory(getHistoryType(), group, this.startDate.getTime()/1000, this.endDate.getTime()/1000, new TEDAsyncCallback<EnergyDataHistoryQueryResult>() {
            @Override
            public void onSuccess(EnergyDataHistoryQueryResult energyDataHistoryQueryResult) {
                logger.fine("history data returned. Drawing");
                historyResult = energyDataHistoryQueryResult;
                VisualizationUtils.loadVisualizationApi(onLoadCallback, BarChart.PACKAGE);
            }
        });


    }

    /**
     * Method used to add the necessary padding before the start of the date query.
     * @param startDate
     * @return
     */
    protected abstract Date fixStartDate(Date startDate);

    /**
     * Method used to add the necessary padding after the end of the date query.
     * @param startDate
     * @return
     */
    protected abstract Date fixEndDate(Date startDate);




    protected BarChart.Options createOptions() {
        DateTimeFormat dateTimeFormat = getDateTimeFormat();
        StringBuffer title = new StringBuffer();
        title.append(getHistoryType());
        title.append(" ").append(graphType).append(" for the date range ");
        title.append(dateTimeFormat.format(startDate)).append(" to ");
        title.append(dateTimeFormat.format(endDate));
        logger.fine("TITLE:" + title);

        BarChart.Options options = BarChart.Options.create();
        options.setWidth(820);
        options.setHeight(520);
        options.set3D(true);
        options.setTitle(title.toString());
        options.setBackgroundColor("transparent");
        options.setTitleColor("#FFFFFF");
        options.setMin(0);


        return options;
    }


    protected AbstractDataTable createTable() {
        DataTable data = DataTable.create();
        data.addColumn(AbstractDataTable.ColumnType.STRING, "Task");


        //Only show NET if we have more than one gateway in the group.
        if (historyResult.getGatewayList().size() > 1)
        {
            data.addColumn(AbstractDataTable.ColumnType.NUMBER, "Total " + graphType);

        }


        for (Gateway gateway: historyResult.getGatewayList()) {
            data.addColumn(AbstractDataTable.ColumnType.NUMBER, gateway.getDescription());
        }



        int rowCount = historyResult.getNetHistoryList().size();
        if (logger.isLoggable(Level.FINE)) logger.fine("Total Results:" + rowCount);
        data.addRows(rowCount);

        for (int i=0; i < rowCount; i++)
        {
            logger.fine("____"+historyResult.getNetHistoryList().get(i).getHistoryDate() + " " + historyResult.getNetHistoryList().get(i).getHistoryDate().getTime());

            String title = getDateTimeFormat().format(historyResult.getNetHistoryList().get(i).getHistoryDate().getTime());

            //Set the month
            int col = 0;
            data.setValue(i, col++, title);

            //Only show NET if we have more than one gateway in the group.
            if (historyResult.getGatewayList().size() > 1)
            {
                if (graphType.equals(Enums.GraphType.ENERGY)) data.setValue(i, col++, historyResult.getNetHistoryList().get(i).getEnergy()/1000);
                else data.setValue(i, col++, historyResult.getNetHistoryList().get(i).getCost());
            }


            for (Gateway gateway: historyResult.getGatewayList()) {
                List<EnergyDataHistory> gwList =historyResult.getGatewayHistoryList().get(gateway.getId());
                if (graphType.equals(Enums.GraphType.ENERGY))  data.setValue(i, col++, gwList.get(i).getEnergy()/1000);
                else  data.setValue(i, col++, gwList.get(i).getCost());
            }

        }

        return data;
    }


    protected abstract DateTimeFormat getDateTimeFormat();
    protected abstract Enums.HistoryType getHistoryType();

}
