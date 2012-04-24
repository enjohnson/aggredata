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

package com.ted.aggredata.client.panels.graph.minute;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ted.aggredata.client.events.GraphOptionsChangedEvent;
import com.ted.aggredata.client.panels.graph.GraphOptionChangeable;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.util.StringUtil;
import com.ted.aggredata.model.Enums;

import java.util.Date;
import java.util.logging.Logger;

public class MinutePanel extends Composite implements GraphOptionChangeable{

    static Logger logger = Logger.getLogger(MinutePanel.class.toString());

    GraphOptionsChangedEvent event;
    final DashboardConstants dashboardConstants = DashboardConstants.INSTANCE;
    final NumberFormat currencyFormat = NumberFormat.getCurrencyFormat();
    final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");

    interface MyUiBinder extends UiBinder<Widget, MinutePanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel graphPanel;
    @UiField
    Label graphTitle;


    @Override
    public void onGraphOptionChange(GraphOptionsChangedEvent event) {
        this.event = event;
        updateTitle();
    }

    /**
     * Updates the title on the page
     */
    private void updateTitle(){
        StringBuffer title = new StringBuffer();
        String interval =  Enums.HistoryType.MINUTE.toString();
        if (event.getInterval() > 1)  interval = event.getInterval() + " " + StringUtil.toTitleCase(interval);
        else interval = StringUtil.toTitleCase(interval);

        title.append(interval);
        title.append(" ");

        if (event.getGraphType().equals(Enums.GraphType.COST)) title.append(dashboardConstants.graphCost());
        else if (event.getGraphType().equals(Enums.GraphType.ENERGY)) title.append(dashboardConstants.graphEnergy());

        title.append(" ").append(dashboardConstants.graphDateRange()).append(" ");
        title.append(dateTimeFormat.format(event.getStartDate())).append(" ").append(dashboardConstants.graphTo()).append(" ");

        Date titleEndDate = new Date(event.getEndDate().getTime());
        title.append(dateTimeFormat.format(titleEndDate));

        graphTitle.setText(title.toString());
    }



    public MinutePanel() {
        initWidget(uiBinder.createAndBindUi(this));

    }

}
