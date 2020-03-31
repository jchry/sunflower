# sunflower简介

Java应用程序零侵入时时监控以及在线诊断。

异常监控；

Http监控；

JVM监控；

JDBC监控；

Redis监控；

MongoDB监控；

CPU监控；

分布式追踪；

# sunflower模块说明

sunflower-client：客户端（spring、spring-mvc项目），负责监控数据上报；

sunflower-spring-boot-starter：客户端(spring-boot、spring-cloud项目)，负责监控数据上报；

sunflower-agent：客户端（在线诊断客户端），线上问题诊断agent；

sunflower-server：服务端，负责监控数据的分析，报警，存储；

sunflower-home：后台管理，负责配置，报表展示；

sunflower-core：核心模块；

sunflower-alarm：报警模块；

