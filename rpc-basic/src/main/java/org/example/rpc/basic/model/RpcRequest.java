package org.example.rpc.basic.model;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {
    // 服务名称
    private String serviceName;
    // 方法名称
    private String methodName;
    // 参数类型列表
    private Class<?>[] parameterTypes;
    // 参数列表
    private Object[] parameters;
}
