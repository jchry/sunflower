#!/bin/bash
source /etc/profile

cd /u01/projectCAR/service/sunflower-server/
project=sunflower-server-1.0.0.jar

#kill -9
pid=`ps -ef|grep ${project}|grep -v grep|awk '{print $2}'`
if [ ! -n "$pid" ]; then
    printf "not found $pid\n"
else
    printf "$pid stopping ....\n"
    kill -9 $pid
    printf "kill -9 $pid....\n"
fi
#sleep 3

export JAVA_OPTS="
  -server
  -Xms1024M
  -Xmx1024M
  -Xmn512M
  -Xss256K
  -XX:SurvivorRatio=8
  -XX:MetaspaceSize=256m
  -XX:MaxMetaspaceSize=256m
  -XX:TargetSurvivorRatio=80
  -XX:MaxTenuringThreshold=10
  -XX:+UseParNewGC
  -XX:+UseConcMarkSweepGC
  -XX:+CMSClassUnloadingEnabled
  -XX:+UseCMSCompactAtFullCollection
  -XX:CMSFullGCsBeforeCompaction=2
  -XX:-CMSParallelRemarkEnabled
  -XX:+DisableExplicitGC
  -XX:CMSInitiatingOccupancyFraction=80
  -XX:SoftRefLRUPolicyMSPerMB=0
  -XX:+DisableExplicitGC
  -XX:+PrintGC
  -XX:+PrintGCDetails
  -XX:+PrintGCDateStamps
  -XX:+PrintGCTimeStamps
  -XX:+PrintGCApplicationConcurrentTime
  -XX:+PrintGCApplicationStoppedTime
  -XX:+PrintHeapAtGC
  -XX:+UnlockDiagnosticVMOptions
  -XX:ParGCCardsPerStrideChunk=32768
  -XX:-OmitStackTraceInFastThrow
  -XX:+PrintTenuringDistribution
  -Xloggc:/u01/sunflower-perf-collector/log/gc.log"

export JAVA_HOME=/usr/local/java
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
nohup /usr/local/java/bin/java ${JAVA_OPTS} -jar ${project} perfCollecor --spring.profiles.active=test > nohup.out 2>&1 &
