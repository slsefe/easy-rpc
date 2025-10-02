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

    public ServiceMetaInfo(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    public String getServiceAddress() {
        return String.format("%s:%s", serviceHost, servicePort);
    }
}

