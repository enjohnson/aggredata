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

package com.ted.aggredata.server.services.impl;

import com.ted.aggredata.model.*;
import com.ted.aggredata.server.services.EmailService;

import com.ted.aggredata.server.services.UserService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Transactional
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    UserService userService;


    static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);



    @Override
    public void sendResetPassword(final User user) {
        if (logger.isInfoEnabled()) logger.info("Send Reset Password requested for " + user);
        //Generate a new password
        String password = KeyGenerator.generateSecurityKey(8);

        //Save the password on the account
        userService.changePassword(user, password);

        //Load up the template
        final Map model = new HashMap();
        model.put("user", user);
        model.put("serverInfo", serverInfo);
        model.put("password", password);
        final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/ted/aggredata/emailTemplates/resetPasswordEmail.vm", model);

        //Create the mime message
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                if (user.getUsername().equals("admin")) {
                    logger.warn("Resetting the ADMIN password");
                    message.setTo(serverInfo.getAdminEmailAddress());
                } else {
                    message.setTo(user.getUsername());
                }

                message.setFrom(serverInfo.getFromAddress());
                message.setSubject("[AggreData] Password Reset Request");
                message.setText(text, true);
            }
        };

        this.mailSender.send(mimeMessagePreparator);


    }
}
