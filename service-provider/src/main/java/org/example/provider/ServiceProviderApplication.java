package org.example.provider;

import org.example.common.service.UserService;
import org.example.rpc.basic.bootstrap.ServiceProviderBootstrap;
import org.example.rpc.basic.model.ServiceRegistryInfo;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderApplication {
    public static void main(String[] args) {
        List<ServiceRegistryInfo<?>> serviceRegistryInfoList = new ArrayList<>();
        ServiceRegistryInfo<?> userServiceRegistryInfo = new ServiceRegistryInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegistryInfoList.add(userServiceRegistryInfo);

        ServiceProviderBootstrap.init(serviceRegistryInfoList);
    }
}
