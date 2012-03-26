package com.ted.aggredata.client.panels.profile.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.resources.lang.DashboardConstants;

import java.util.logging.Logger;

public class SettingsPanel extends Composite {

    static Logger logger = Logger.getLogger(SettingsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, SettingsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    @UiField
    Label titleLabel;
    @UiField
    Label instructionLabel;

    public SettingsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        titleLabel.setText(dashboardConstants.settingsTitle());
        instructionLabel.setText(dashboardConstants.settingsInstructions());
    }

}
