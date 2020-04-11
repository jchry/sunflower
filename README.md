# sunflower简介

Java应用零侵入监控。

异常监控；

分布式链路追踪；

Http监控；

JVM监控；

JDBC监控；

CPU监控；

# sunflower模块说明

## sunflower-sniffer：客户端，负责监控数据采集上报；

sunflower-agent: agent入口；

bootstrap: agent实现代码；

bootstrap: 核心代码；

bootstrap-plugins: 每种监控以插件方式实现；

## sunflower-collector：服务端，负责监控数据存储；

collector-starter: 服务端启动；

collector-core: 核心服务实现；

## sunflower-home：后台管理，负责配置、报表、监控等；

## sunflower-commons：客户端、服务端、后台管理公用实现；
