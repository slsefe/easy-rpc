package org.example.rpc.basic.registry;

import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.model.ServiceMetaInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @After
    public void destroy() {
        registry.destroy();
    }

    @Test
    public void register_and_service_discovery() throws Exception {
        ServiceMetaInfo serviceMetaInfo1 = ServiceMetaInfo.builder()
                .serviceName("service1")
                .serviceVersion("1.0")
                .serviceHost("localhost")
                .servicePort(1234)
                .build();
        registry.register(serviceMetaInfo1);

        ServiceMetaInfo serviceMetaInfo2 = ServiceMetaInfo.builder()
                .serviceName("service1")
                .serviceVersion("1.0")
                .serviceHost("localhost")
                .servicePort(1235)
                .build();
        registry.register(serviceMetaInfo2);

        ServiceMetaInfo serviceMetaInfo3 = ServiceMetaInfo.builder()
                .serviceName("service1")
                .serviceVersion("2.0")
                .serviceHost("localhost")
                .servicePort(1235)
                .build();
        registry.register(serviceMetaInfo3);
    }

    @Test
    public void unregister() {
        ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                .serviceName("service1")
                .serviceVersion("1.0")
                .serviceHost("localhost")
                .servicePort(1234)
                .build();
        registry.unregister(serviceMetaInfo);
    }

    @Test
    public void service_discovery() {
        ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                .serviceName("service1")
                .serviceVersion("1.0")
                .build();
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        assertNotNull(serviceMetaInfos);
    }
}
