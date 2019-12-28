#!/bin/bash
cd /u01/projectCAR/service/passenger-proxy-api/

kill -9 `ps -ef|grep passenger-proxy-api-1.0-SNAPSHOT.jar|grep -v grep|awk '{print $2}'`
