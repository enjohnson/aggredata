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

package com.ted.aggredata.server.guiServiceImpl;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/***
 * Simple suplerclass of all AggreData2 RemoteServiceServlets. This is primarialy intended
 * to allow for Spring integration as well as include some common utility classes.
 *
 * Thanks to Max Matveev for posting this solution to his blog: http://blog.maxmatveev.com/2011/02/simple-spring-bean-autowiring-in-gwt.html
 *
 */
public class SpringRemoteServiceServlet extends RemoteServiceServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContextUtils.
                getRequiredWebApplicationContext(getServletContext()).
                getAutowireCapableBeanFactory().
                autowireBean(this);
    }
}
