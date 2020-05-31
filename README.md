# sunflower简介

Java分布式系统监控、追踪、在线诊断。

- 分布式链路追踪

- 异常监控

- HTTP监控

- JVM监控

- MySql监控

- CPU监控

- 报警

# sunflower模块说明

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

负责配置、报表、监控展示；

## sunflower-commons

客户端、服务端、后台管理公共代码；
