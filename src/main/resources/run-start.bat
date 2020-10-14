title item-lwzx-autoword
@echo off
echo  use cmd mode
set JAVA_HOME="Y:\Documents\win\Java\jdk8"
echo set JAVA_HOME=Y:\Documents\win\Java\jdk8
set JAVACMD=%JAVA_HOME%\bin\java
set JAVADBG=%JAVA_HOME%\bin\java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=11001,server=y,suspend=n"
set CONF_DIR=%1 

set APP_CLASS=item-lwzx-autoword-0.0.1-SNAPSHOT.jar

%JAVACMD% -Xms5096m -Xmx5096m -XX:SurvivorRatio=4 -XX:ErrorFile=d:/hlog/error.log -XX:+UseParNewGC -XX:ParallelGCThreads=8  -XX:+DisableExplicitGC  -Djava.awt.headless=true  -Dsun.rmi.dgc.server.gcInterval=60000 -Dsun.rmi.dgc.client.gcInterval=60000 -XX:+UseConcMarkSweepGC -XX:MaxTenuringThreshold=15 -XX:+UseAdaptiveSizePolicy  -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection  -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -jar %APP_CLASS% 

pause;