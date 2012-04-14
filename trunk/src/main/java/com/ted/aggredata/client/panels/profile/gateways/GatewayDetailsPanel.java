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

package com.ted.aggredata.client.panels.profile.gateways;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GatewayDetailsPanel extends Composite {

    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);

    static Logger logger = Logger.getLogger(GatewayDetailsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GatewayDetailsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    TextBox descriptionField;
    @UiField
    Label descriptionFieldError;
    @UiField
    TextBox custom1Field;
    @UiField
    Label custom1FieldError;
    @UiField
    TextBox custom2Field;
    @UiField
    Label custom2FieldError;
    @UiField
    TextBox custom3Field;
    @UiField
    Label custom3FieldError;
    @UiField
    TextBox custom4Field;
    @UiField
    Label custom4FieldError;
    @UiField
    TextBox custom5Field;
    @UiField
    Label custom5FieldError;
    @UiField
    Label serialNumberFieldError;
    @UiField
    TextBox serialNumberField;
    @UiField
    GatewaysMTUPanel gatewayMTUPanel;

    Gateway gateway;
    Integer gatewayHashCode = 0;
    List<Gateway> gatewayList = new ArrayList<Gateway>();


    ChangeHandler saveChangeHanlder = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
            if (gateway != null) doSave();
        }
    };

    public GatewayDetailsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));

        descriptionField.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                validate();
            }
        });

        serialNumberField.setReadOnly(true);
        serialNumberField.setEnabled(false);
        descriptionField.addChangeHandler(saveChangeHanlder);
        custom1Field.addChangeHandler(saveChangeHanlder);
        custom2Field.addChangeHandler(saveChangeHanlder);
        custom3Field.addChangeHandler(saveChangeHanlder);
        custom4Field.addChangeHandler(saveChangeHanlder);
        custom5Field.addChangeHandler(saveChangeHanlder);

    }

    public void setGatewayList(List<Gateway> userGatewayList) {
        this.gatewayList = userGatewayList;
    }


    /**
     * Sets all the fields of this panel to be enabled or disabled
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {

        descriptionField.setEnabled(enabled);
        custom1Field.setEnabled(enabled);
        custom2Field.setEnabled(enabled);
        custom3Field.setEnabled(enabled);
        custom4Field.setEnabled(enabled);
        custom5Field.setEnabled(enabled);


    }


    public boolean validate(){
        boolean valid = true;
        descriptionFieldError.setText("");

        if (gateway == null) return true;
        if (descriptionField.getText().trim().length() == 0) {
            valid = false;
            descriptionFieldError.setText("Required");
        }

        for (Gateway g: gatewayList){
            if (!g.getId().equals(gateway.getId()) && g.getDescription().toLowerCase().equals(descriptionField.getText().trim().toLowerCase())){
                valid = false;
                descriptionFieldError.setText("Already Used");
            }
        }

        return valid;
    }

    private void doSave()
    {
        if (validate())
        {
            gateway.setDescription(descriptionField.getText().trim());
            gateway.setCustom1(custom1Field.getText().trim());
            gateway.setCustom2(custom2Field.getText().trim());
            gateway.setCustom3(custom3Field.getText().trim());
            gateway.setCustom4(custom4Field.getText().trim());
            gateway.setCustom5(custom5Field.getText().trim());
            if (gateway.hashCode() != gatewayHashCode){
                logger.info("gateway is dirty. Saving " + gateway);
                gatewayService.saveGateway(gateway, new TEDAsyncCallback<Gateway>() {
                    @Override
                    public void onSuccess(Gateway gateway) {
                        gatewayHashCode = gateway.hashCode();
                    }
                });
            }
        }
    }

    public void setGateway(final Gateway gateway)
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Setting gateway " + gateway);
        setEnabled(gateway!=null);
        serialNumberField.setValue(Long.toHexString(gateway.getId()).toUpperCase());
        descriptionField.setValue(gateway.getDescription());
        custom1Field.setValue(gateway.getCustom1());
        custom2Field.setValue(gateway.getCustom2());
        custom3Field.setValue(gateway.getCustom3());
        custom4Field.setValue(gateway.getCustom4());
        custom5Field.setValue(gateway.getCustom5());
        this.gateway = gateway;
        gatewayHashCode = gateway.hashCode();

        gatewayService.findMTU(gateway, new TEDAsyncCallback<List<MTU>>() {
            @Override
            public void onSuccess(List<MTU> mtus) {
                gatewayMTUPanel.setMTUList(gateway, mtus);
            }
        });

        validate();
    }


}
