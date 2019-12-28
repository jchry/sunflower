#!/bin/bash
cd /u01/projectCAR/service/sunflower-server/

kill -9 `ps -ef|grep sunflower-server-1.0.0.jar|grep -v grep|awk '{print $2}'`
