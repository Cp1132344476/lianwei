# Spring Cloud

**微服务架构4个核心问题**

1. 服务很多，客户端该怎么访问？
2. 这么多服务，服务之间如何通信
3. 这么多服务，如何治理
4. 服务挂了怎么办

**解决方案：**

Spring Cloud   生态！

1. Spring Cloud NetFlix		一站式解决方案

   api网关，zuul组件

   Feign ---HttpClient----  http通信方式，同步，阻塞

   服务注册发现：Eureka

   熔断机制：Hystrix

2. Apache Dubbo Zookeeper     半自动，需要整合别人的

   API：没有，找第三方组件，或者自己实现

   Dubbo

   Zookeeper

   没有：借助Hystrix

   这个方案并不完善

3. Spring Cloud Alibaba       一站式解决方案

新概念：服务网格 Sever Mesh

## 微服务的优缺点

**优点：**

- 每个服务足够内聚，足够小，代码容易理解，这样能聚焦一个指定的业务功能或者业务需求；
- 开发简单，开发效率提高，一个服务可能就是专一的只干一件事；
- 微服务能够被小团队单独开发，这个小团队是2-5人的开发人员组成
- 微服务是松耦合的，是有功能意义的服务，无论是在开发阶段或者部署阶段都是独立的；
- 无服务能够使用不同的语言开发
- 易于和第三方集成，微服务允许容易且灵活的方式集成自动部属，通过持续集成工具，如jebkins，Hudson，bamboo
- 微服务易于被一个开发人员理解，修改和维护，这样小团队能够更关注自己的工作成果。无需通过合作才能体现价值
- 微服务允许你利用融合最新技术
- 微服务只是业务逻辑的代码，不会和HTML，CSS或者其他界面混合
- 每个微服务都有自己的存储能力，可以有自己的数据库，也可以有统一的数据库

**缺点：**

- 开发人员要处理分布式系统的复杂性
- 多服务运维难度，随着服务的增加，运维的压力也在增大
- 系统部署依赖
- 服务间通信成本
- 数据一致性
- 系统集成测试
- 性能监控

## SpringCloud和SpringBoot关系

- Springboot专注于快速方便的开发单个个体微服务
- SpringCloud是关注全局的微服务协调整理治理框架，他将Springboot开发的一个个单体微服务整合并管理起来，为各个微服务之间提供：配置管理，服务发现，断路器，路由，微代理，事件总线，全局锁，决策竞选，分布式会话等等集成服务。
- Springboot可以离开Springcloud独立使用，开发项目，但是SpringCloud离不开SpringBoot属于依赖关系
- Springboot专注于快速、方便的开发单个个体微服务，SpringCloud关注全局的服务治理框架