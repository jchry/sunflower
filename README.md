# 一、sunflower 简介

Java 分布式系统监控。

- 分布式链路追踪

- 异常监控

- HTTP 监控

- JVM 监控

- MySql 监控

- CPU 监控

- 报警

- 中间件监控

# 二、sunflower 模块说明

## sunflower-sniffer（客户端）

负责监控数据采集上报；

- agent: agent入口

- agent-core: agent核心实现

- sdk-plugins: 每种监控以插件方式实现

## sunflower-collector（服务端）

负责监控数据存储；

- collector-starter: 服务端启动

- collector-core: 核心服务实现

## sunflower-home（后台管理）

负责配置、报表、监控数据展示等；

## sunflower-commons

客户端、服务端、后台公共代码库；
