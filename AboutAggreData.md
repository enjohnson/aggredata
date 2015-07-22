# Introduction #

The Energy Detective's AggreData is an open-source data platform that will allow a user(s) to aggregate energy-data from a single/multiple/group/mass of TED's on their own server.

## Building AggreData ##

TED AggreData can be built using maven 3.0.x available here: http://http://maven.apache.org/

**mvn -Dmaven.test.skip=true clean install**

## Getting Started ##

1. Install and configure a MySQL instance.

2. Run database/scripts/db\_create.sql on the MySQL instance.

3. MySQL must be configured for timezone support. On most linux systems the command is:
> mysql\_tzinfo\_to\_sql /usr/share/zoneinfo | mysql -u root -p mysql

> Please refer to the following page for more information regarding timezone configuration:
> > http://dev.mysql.com/doc/refman/4.1/en/mysql-tzinfo-to-sql.html


4. Read config/README.txt regarding the configuration files.

5. Copy aggredata.log4j.xml to your application container's home directory.

6. If you need to change the default JDBC settings, copy aggredata.properties to your application container's home directory.

7. Deploy aggredata.war to you application container/

8. Go to http://<container ip address>/aggredata to access the

> application (e.g. http://127.0.0.1:8080/aggredata

9. The default username/password is admin/admin

## To deploy an existing application ##

1. Configure a MySQL instance

2. Run database/scripts/db\_create.sql on the MySQL instance.

3. MySQL must be configured for timezone support. On most linux systems the command is:
> mysql\_tzinfo\_to\_sql /usr/share/zoneinfo | mysql -u root -p mysql

> Please refer to the following page for more information regarding timezone configuration:
> > http://dev.mysql.com/doc/refman/4.1/en/mysql-tzinfo-to-sql.html

4. Read config/README.txt regarding the configuration files.

5. Copy aggredata.log4j.xml to your application container's home directory

6. If you need to change the default JDBC settings, copy aggredata.properties to your application container's home directory.

7. Deploy aggredata.war to you application container

8. Go to http://<container ip address>/aggredata to access the application (e.g. http://127.0.0.1:8080/aggredata

9. The default username/password is admin/admin

## To deploy on a new windows-based platform ##

1.  Download and install MySQL Community Edition.


> a.  Download from:  http://www.mysql.com/downloads/mysql/

> b.  Install MySQL.  Choose the presented default features during the installation.

> c.  (Optional) Download the MySQL Workbench from:  http://dev.mysql.com/downloads/workbench/

> d.  If downloaded, install MySQL Workbench.

> e.  Once installed, start the MySQL service.

2.  Execute the /database/scripts/db\_create.sql script file against your MySQL instance.

> a.  In MySQL Workshop this can by done by connecting to the instance, then in the automatically opened "SQL File" tab click open, then browse to the script and select it. Ensure that the "DROP" statement immediately following the first "CREATE" statement is commented out.  Then click the execute button at the top of the window.

> b.  In MySQL shell this can be done by using the command:  \. [aggredata\_path](aggredata_path.md)\database\scripts\db\_create.sql

3.  Download and install the POSIX time zone tables for MySQL, since Windows doesnt provide them.

> a.  Download from:  http://dev.mysql.com/downloads/timezones.html

> b.  Stop the MySQL database service.

> c.  Unzip to your mysql data folder.  Note that this may not be in "Program Files"; check the hidden path "C:\ProgramData\MySQL\MySQL Server 5.5\data\mysql" also.  The MySQL data folder desired is the one which contains the "aggredata" folder as well as "mysql".

> d.  Restart the MySQL database service.

4.  Download and install Java JDK 1.7.

> a.  Download from:  http://www.oracle.com/technetwork/java/javase/downloads/index.html

> b.  After installing, add a JAVA\_HOME system variable by opening the control panel, System, Advanced System Options, then clicking Environment Variables.  Add a new system variable called "JAVA\_HOME" with a value of the full path to your JDK installation folder.

5.  Download and configure Apache Maven 3.x.

> a.  Download from:  http://maven.apache.org/download.html

> b.  Unzip apache-maven-3.0.x.zip to a location of your choice.

> c.  Add the bin folder from the resulting maven folder to your path by opening the control panel, System, Advanced System Options, then clicking Environment Variables.  Edit the PATH system variable and append the appropriate path preceeded by a semicolon (;).
> > Example:  [existing\_path](existing_path.md);E:\Data\apache-maven-3.0.4\bin


> d.  Confirm successful configuration by opening a command prompt and running the following command:
> > mvn --version

6.  Download and configure Apache Tomcat 7.x application server.


> a.  Download from:  http://tomcat.apache.org/download-70.cgi

> b.  Unzip the downloaded archive to a location of your choice.

7.  Compile and deploy Aggredata

> a.  Build.bat located in the root of the aggredata distribution will build the application automatically. Alternatively it may be built from the command line at the aggredata root folder level by executing the command:  mvn -Dmaven.test.skip=true clean install

> b.  A "target" folder will have been created in the aggredata root folder level.  Copy the file
> > "aggredata.war" from this folder to the /webapps folder of your Apache Tomcat installation folder.


> c.  Copy aggredata.log4j.xml from the aggredata/config folder to the /bin folder of your Apache Tomcat installation folder.

> d.  Execute the file "Startup.bat" in the /bin folder of your Apache Tomcat installation folder.

8.  Open a web browser to http://127.0.0.1:8080/aggredata to log in and begin logging data.