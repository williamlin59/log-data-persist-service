For running this app simply add -Djava.awt.headless=false as vm option to run this application
 
If you want to visualize data from hsqldb then run app with /lib/hsqldb.jar org.hsqldb.util.DatabaseManager as dependency

This app uses streamEx and spring boot to perform data streaming from file as well as to hsqldb. Spring transactional manager is used for

transaction management. 
