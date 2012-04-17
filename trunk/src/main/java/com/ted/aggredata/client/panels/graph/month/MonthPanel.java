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

package com.ted.aggredata.client.panels.graph.month;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.ted.aggredata.client.panels.graph.GraphOptionChangeable;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.Group;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonthPanel extends Composite implements GraphOptionChangeable{

    static Logger logger = Logger.getLogger(MonthPanel.class.toString());



    interface MyUiBinder extends UiBinder<Widget, MonthPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    VerticalPanel graphPanel;
    @UiField
    Label optionInfoLabel;


    public MonthPanel() {
        initWidget(uiBinder.createAndBindUi(this));


        Runnable onLoadCallback = new Runnable() {
            public void run() {

                // Create a pie chart visualization.
                PieChart pie = new PieChart(createTable(), createOptions());
                pie.addSelectHandler(createSelectHandler(pie));
                graphPanel.add(pie);
            }
        };


        VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);


    }

    @Override
    public void onGraphOptionChange(Group group, Date startDate, Date endDate, Enums.GraphType graphType) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Graph Option Change Called: " + group + " " + startDate + " " + endDate + " " + graphType);
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
        optionInfoLabel.setText("Graphing  " + graphType  +" for the group" + group.getDescription() +" and the date range " +dateTimeFormat.format(startDate) + " to " + dateTimeFormat.format(endDate));
    }


    private SelectHandler createSelectHandler(final PieChart chart) {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                String message = "";

                // May be multiple selections.
                JsArray<Selection> selections = chart.getSelections();

                for (int i = 0; i < selections.length(); i++) {
                    // add a new line for each selection
                    message += i == 0 ? "" : "\n";

                    Selection selection = selections.get(i);

                    if (selection.isCell()) {
                        // isCell() returns true if a cell has been selected.

                        // getRow() returns the row number of the selected cell.
                        int row = selection.getRow();
                        // getColumn() returns the column number of the selected cell.
                        int column = selection.getColumn();
                        message += "cell " + row + ":" + column + " selected";
                    } else if (selection.isRow()) {
                        // isRow() returns true if an entire row has been selected.

                        // getRow() returns the row number of the selected row.
                        int row = selection.getRow();
                        message += "row " + row + " selected";
                    } else {
                        // unreachable
                        message += "Pie chart selections should be either row selections or cell selections.";
                        message += "  Other visualizations support column selections as well.";
                    }
                }

                //Window.alert(message);
            }
        };
    }


    private Options createOptions() {
        Options options = Options.create();
        options.setWidth(400);
        options.setHeight(240);
        options.set3D(true);
        options.setTitle("Current Month Cumulative Usage");
        options.setBackgroundColor("transparent");
        options.setTitleColor("#FFFFFF");
        return options;
    }

    private AbstractDataTable createTable() {
        DataTable data = DataTable.create();
        data.addColumn(ColumnType.STRING, "Task");
        data.addColumn(ColumnType.NUMBER, "Hours per Day");
        data.addRows(3);
        data.setValue(0, 0, "Work");
        data.setValue(0, 1, 20);
        data.setValue(1, 0, "Sleep");
        data.setValue(1, 1, 2);
        data.setValue(2, 0, "Party");
        data.setValue(2, 1, 2);

        return data;
    }
}



