package com.ted.aggredata.client.panels.profile.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.LargeButton;

import java.util.logging.Logger;

public class SettingsPanel extends Composite {

    static Logger logger = Logger.getLogger(SettingsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, SettingsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    @UiField Label titleLabel;
    @UiField Label instructionLabel;
    @UiField Label lastNameLabel;
    @UiField TextBox lastNameField;
    @UiField Label lastNameFieldError;
    @UiField Label firstNameLabel;
    @UiField TextBox firstNameField;
    @UiField Label firstNameFieldError;
    @UiField Label companyNameLabel;
    @UiField TextBox companyNameField;
    @UiField Label companyNameFieldError;
    @UiField
    LargeButton saveButton;
    @UiField
    LargeButton resetButton;
    @UiField
    HorizontalPanel buttonPanel;
    @UiField
    VerticalPanel mainPanel;

    public SettingsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        titleLabel.setText(dashboardConstants.settingsTitle());
        instructionLabel.setText(dashboardConstants.settingsInstructions());
        lastNameLabel.setText(dashboardConstants.profileSettingsLastName());
        firstNameLabel.setText(dashboardConstants.profileSettingsFirstName());
        companyNameLabel.setText(dashboardConstants.profileSettingsCompanyName());

        firstNameFieldError.setText("");
        lastNameFieldError.setText("");
        companyNameFieldError.setText("");

        
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (doValidation())
                {
                    Window.alert("Save clicked!");
                }

            }
        });

        
        resetButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.alert("Reset clicked!");
            }
        });
        
        mainPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);


    }

    /**
     * Performs the field validation. Returns false if any of the fields fail validation
     * @return
     */
    private boolean doValidation()
    {
        boolean isValid = true;

        firstNameFieldError.setText("");
        lastNameFieldError.setText("");
        companyNameFieldError.setText("");

        if (firstNameField.getText().trim().length()==0)
        {
            isValid = false;
            firstNameFieldError.setText("Required");
        }

        if (lastNameField.getText().trim().length()==0)
        {
            isValid = false;
            lastNameFieldError.setText("Required");
        }

        return isValid;

    }
}
