package com.ted.aggredata.client.panels.graph.month;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.resources.lang.DashboardConstants;

import java.util.logging.Logger;

public class MonthPanel extends Composite {

    static Logger logger = Logger.getLogger(MonthPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, MonthPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);
    @UiField
    Label titleLabel;
    @UiField
    Label instructionLabel;


    public MonthPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        titleLabel.setText(dashboardConstants.monthTitle());
        instructionLabel.setText(dashboardConstants.monthInstructions());

    }

}
