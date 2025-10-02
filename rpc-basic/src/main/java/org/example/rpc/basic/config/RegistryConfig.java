package org.example.rpc.basic.config;

import lombok.Data;

@Data
public class RegistryConfig {
    private String registry = "etcd";
    private String address = "https://localhost:2380";
    private String username;
    private String password;
    private Long timeout = 10000L;
}
