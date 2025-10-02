package org.example.rpc.basic.config;

import lombok.Data;
import org.example.rpc.basic.serializer.SerializerKeys;

// RPC框架配置
@Data
public class RpcConfig {
    private String name = "easy-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private int serverPort = 8080;

    private boolean mock = false;

    private RegistryConfig registry = new RegistryConfig();

    // 序列化器
    private String serializer = SerializerKeys.JDK;

}
