package org.example.rpc.basic.fault.tolerant;

import org.example.rpc.basic.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 */
public interface TolerantStrategy {

    RpcResponse doTolerant(Map<String, Object> context, Exception exception);
}
