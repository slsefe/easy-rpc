# easy-rpc

基于Vert.x实现一个简易的RPC框架。

## 模块介绍

- common: 公共依赖
- rpc-basic: 简易版RPC框架
- service-consumer: 服务消费者
- service-provider: 服务提供者

- common
    - 定义数据模型、服务接口
- rpc-basic模块实现简易版RPC框架，包括
    - 动态生成服务代理类
    - 序列化和反序列化
    - 本地注册器
    - HTTP请求处理器
- service-provider：
    - 注册提供的服务到本地注册器
    - 启动web服务器，监听端口
    - 实现common模块定义的服务接口
- service-consumer
    - 从服务代理工厂获取动态代理对象
    - 调用动态代理对象的方法

## 框架核心功能

- 基于动态代理和Vert.x实现的RPC
- 应用配置中心
- 提供了etcd服务注册中心，支持自定义注册中心
- 提供了JDK原生、Json、Kryo、Hessian序列化器，支持通过配置指定序列化器，也支持自定义序列化器
- 提供了轮询、随机、一致性哈希负载均衡器，支持通过配置指定负载均衡器，也支持自定义负载均衡器
- 提供了不重试、固定间隔重试机制，支持通过配置指定重试机制，也支持自定义重试机制
- 提供了FailBack、FailFast、FailOver、FailSafe容错机制，支持通过配置指定容错机制，也支持自定义容错机制

## 流程

1. 启动服务提供者，将服务注册到注册中心（ServiceProviderApplication类）
2. 服务消费者，根据调用的服务、方法、参数，生成调用服务的动态代理类（ServiceConsumerApplication类）
3. 动态代理类处理流程（ServiceProxy类） 
   1. 构造请求体，并序列化
   2. 从注册中心获取服务提供者的地址
   3. 发送HTTP请求，获取响应
   4. 封装响应，返回结果
4. 服务提供者的处理流程（）
   1. 反序列化请求为对象，从请求对象中获取参数
   2. 根据服务名称从本地注册器中获取实现类
   3. 根据反射调用方法，得到返回结果
   4. 对返回结果封装和序列化，写入到响应

### 服务注册

先使用服务提供者本地作为服务注册中心，存储服务注册信息，key为服务名称，value为服务的实现类。

如何调用：根据服务名称获取实现类，通过反射进行方法调用。

使用etcd作为注册中心。

#### etcd

下载安装
```shell
brew update
brew install etcd
etcd --version
```
启动
```shell
etcd
```

#### etcd-keeper 

可视化工具: https://github.com/evildecay/etcdkeeper/

安装
```shell
git clone
cd 
build.sh release.sh
```
启动
```shell
./etcdkeeper -p 8081
```

#### etcd java客户端

etcd java 客户端：https://github.com/etcd-io/jetcd

### 负载均衡器

### 序列化器

### 重试

### 容错