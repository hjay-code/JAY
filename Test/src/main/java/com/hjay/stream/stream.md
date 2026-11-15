1. 处理流程 StreamDemo ：创建流 -> 中间操作 -> 新流 -> 终止操作 -> 结果
2. 流可以开启并发操作
3. 中间操作

4. flow jdk9 新增的流式处理,响应式处理,使用 发布/订阅 模型，设计模式是观察者模式,默认是异步模式
4.1. Flow.Publisher  发布者，SubmissionPublisher 提供的默认简单发布者
4.2. Flow.Subscriber 订阅者，使用被压模式，主动获取消息
4.3. Flow.Subscription 订阅关系，一般绑定在 订阅者 中，可以 拉取消息，取消订阅
4.4. Flow.Processor 处理器，控制发布者 和 订阅者 中的数据处理，形成 职责链