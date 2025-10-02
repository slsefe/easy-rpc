package org.example.rpc.basic.config;

import lombok.Data;

// RPC框架配置
@Data
public class RpcConfig {
    private String name = "easy-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private int serverPort = 8080;

    private boolean mock = false;

    private RegistryConfig registry = new RegistryConfig();

}
