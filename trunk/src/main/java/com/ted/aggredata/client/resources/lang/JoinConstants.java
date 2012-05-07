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

package com.ted.aggredata.client.resources.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface JoinConstants extends Constants {

    public static final JoinConstants INSTANCE = GWT.create(JoinConstants.class);

    String passwordLabel();

    String userNameLabel();

    String confirmPasswordLabel();

    String confirmUserNameLabel();

    String joinHeader();

    String pendingApprovalError();

    String notEmailError();

    String alreadyExists();

    String confirmUsernameError();

    String confirmPasswordError();

    String invalid();

    String noMatch();

    String required();

    String captcha();

    String reloadCaptcha();

    String formErrors();

    String captchaError();

    String captchaRequired();

    String activationTitle();

    String activationDescription();
}
