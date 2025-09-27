# easy-rpc

## 模块介绍

- common: 公共依赖
- rpc-basic: 简易版RPC框架
- service-consumer: 服务消费者
- service-provider: 服务提供者

## 实现原理

### common模块

- 定义实体类User
- 定义接口 UserService 和接口方法 getUser

### 服务提供者

- 引入common模块
- 实现UserService接口
- 创建启动类
