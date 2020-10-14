#!/bin/sh
JAVA_HOME="/usr/local/jdk8"
_jar=item-lwzx-autoword-0.0.1-SNAPSHOT.jar
$JAVA_HOME/bin/java  -Xms1024m -Xmx2048m -Xmn600m -XX:SurvivorRatio=2 -XX:+ScavengeBeforeFullGC -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=8    -jar $_jar   >>/dev/null &
