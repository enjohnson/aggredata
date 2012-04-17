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

package com.ted.aggredata.client.panels.side;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.ted.aggredata.client.events.GraphTypeSelectedEvent;
import com.ted.aggredata.client.events.GraphTypeSelectedHandler;
import com.ted.aggredata.model.Enums;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class TypeSelectionWidget extends Composite {

    interface MyUiBinder extends UiBinder<Widget, TypeSelectionWidget> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private static DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));

    final private HandlerManager handlerManager;
    @UiField
    RadioButton energyRadioButton;
    @UiField
    RadioButton costRadioButton;

    public TypeSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);
        energyRadioButton.setValue(true);

        energyRadioButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (logger.isLoggable(Level.FINE)) logger.fine("Energy Radio Box Clicked");
                handlerManager.fireEvent(new GraphTypeSelectedEvent(Enums.GraphType.ENERGY));
            }
        });


        costRadioButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (logger.isLoggable(Level.FINE)) logger.fine("Cost Radio Box Clicked");
                handlerManager.fireEvent(new GraphTypeSelectedEvent(Enums.GraphType.COST));
            }
        });


    }

    public void setType(Enums.GraphType graphType) {
        energyRadioButton.setValue(graphType.equals(Enums.GraphType.ENERGY));
        costRadioButton.setValue(graphType.equals(Enums.GraphType.COST));
    }

    public Enums.GraphType getType() {
        if (energyRadioButton.getValue()) return Enums.GraphType.ENERGY;
        return Enums.GraphType.COST;
    }


    public HandlerRegistration addGraphTypeSelectedHandler(GraphTypeSelectedHandler handler) {
        return handlerManager.addHandler(GraphTypeSelectedEvent.TYPE, handler);
    }
}
