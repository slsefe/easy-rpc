package org.example.rpc.basic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 服务元信息
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        if (serviceHost.contains("http")) {
            return String.format("%s:%s", serviceHost, servicePort);
        }
        return String.format("http://%s:%s", serviceHost, servicePort);
    }
}

