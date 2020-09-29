# 前言

JDK8，持续完善中......

# 一、sunflower 简介

Java 分布式系统监控。

- 异常监控

- 分布式链路追踪

- HTTP 监控

- JVM 监控

- MySql 监控

- Redis 监控

- MongoDB 监控

- RocketMQ 监控

- ES 监控

- CPU 监控

- 内存监控

- 磁盘 IO 监控

- 系统负载监控

- 监控告警(钉钉推送)

- 自定义监控

# 二、sunflower 模块说明

## sunflower-agent（客户端）

负责监控数据采集上报；

## sunflower-server（服务端）

负责监控数据处理、存储、展示、配置等；

## sunflower-remoting（远程通信）

基于Netty实现基础通信组件；

## sunflower-logging（日志）

内部日志；

## sunflower-common（公共包）

公共工具代码包；

## sunflower-examplse（测试代码）

agent应用、通信等测试demo；
