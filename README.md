# Tokiy : 年糕

> 一个支持手动触发历史快照的 数据清洗工具库.

> Note: 如果公司有诸如 flink / spark 实时平台并且基础设施相对完善, 运维支持相对给力, 建议优先考虑使用公司支持的平台

> Note: 如果你考虑成本/公司也没人和钱去维护一些数据平台 可以考虑这个.

### 样例类

- 见 `tikoy-spring` 下的 TestBootstrap 类.

### 重点概念

- JobFactory :  用于生成数据清洗JOb. 类比 spring-kafka 中的 @KafkaConsumer 的对应任务的生成逻辑.

- JObMapping / ProcessOn / OnInsert / OnDelete ... :  数据处理路由注解 . 类比 spring-web @RequestMapping 概念, 注意差异

- JobSnapshotTrigger : 用于触发快照表实现历史表的计算.  类比成历史数据批处理

