package org.example.rpc.basic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistryInfo<T> {
    private String serviceName;
    private Class<? extends T> implClass;
}
