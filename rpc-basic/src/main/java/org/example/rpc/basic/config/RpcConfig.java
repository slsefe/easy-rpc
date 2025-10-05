package org.example.rpc.basic.config;

import lombok.Data;
import org.example.rpc.basic.fault.retrying.RetryStrategyKeys;
import org.example.rpc.basic.fault.tolerant.TolerantStrategyKeys;
import org.example.rpc.basic.loadbalancer.LoadBalancerKeys;
import org.example.rpc.basic.serializer.SerializerKeys;

// RPC框架配置
@Data
public class RpcConfig {
    private String name = "easy-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private int serverPort = 8080;

    private boolean mock = false;

    private RegistryConfig registryConfig = new RegistryConfig();

    // 序列化器
    private String serializer = SerializerKeys.JDK;

    // 负载均衡算法
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    // 重试策略
    private String retryStrategy = RetryStrategyKeys.NO;

    // 容错策略
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
