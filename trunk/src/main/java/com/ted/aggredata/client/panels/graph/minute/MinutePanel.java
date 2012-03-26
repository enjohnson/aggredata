package com.ted.aggredata.client.panels.graph.minute;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.resources.lang.DashboardConstants;

import java.util.logging.Logger;

public class MinutePanel extends Composite {

    static Logger logger = Logger.getLogger(MinutePanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, MinutePanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);
    @UiField
    Label titleLabel;
    @UiField
    Label instructionLabel;


    public MinutePanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        titleLabel.setText(dashboardConstants.minuteTitle());
        instructionLabel.setText(dashboardConstants.minuteInstructions());

    }

}
