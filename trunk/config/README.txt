AggreData makes use of 2 external config files.

-aggredata.log4j.xml: this file is used to configure runtime logging settings. By default,
AggreData looks at the root directory of the application server for this file. If you would
like to specify a different directory, modify the context-param for the Log4jConfigListener
in web.xml (you can specify a different file path or the classpath).

-aggredata.properties: this file is used to configure various runtime options such as JDBC.
By default, AggreData looks at the root directory of the application server for this file. If
you would like to specify a different directory, modify the following line in the
applicationContext.xml file to point to the appropriate path:

<property name="location" value="file:aggredata.properties" />

If this file is not specified, AggreData will refer to the default values specified in
applicationContext.xml



