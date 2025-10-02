package org.example.rpc.basic.model;

import lombok.Data;

// 服务元信息
@Data
public class ServiceMetaInfo {

    private String serviceName;

    private String serviceVersion = "1.0";

    private String serviceHost;

    private Integer servicePort;

    private String serviceGroup = "default";

}

