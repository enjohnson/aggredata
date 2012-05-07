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

package com.ted.aggredata.client.panels.join;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.Date;


public class CaptchaPanel extends Composite {



    interface MyUiBinder extends UiBinder<Widget, CaptchaPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    VerticalPanel captchaVerticalPanel;
    @UiField
    VerticalPanel captchaImagePanel;
    @UiField
    Label reloadImage;
    @UiField
    TextBox captchaField;

    Image captchaImage;

    public CaptchaPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        reloadCaptchaImage();

        reloadImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                reloadCaptchaImage();
            }
        });

    }


    private void reloadCaptchaImage(){
        captchaImagePanel.clear();
        captchaImage = new Image("/aggredata/captcha.jpg?" + new Date().getTime());
        captchaImage.setSize("130px", "50px");
        captchaImagePanel.add(captchaImage);
    }

    public String getValue() {
        return captchaField.getValue();
    }


}
