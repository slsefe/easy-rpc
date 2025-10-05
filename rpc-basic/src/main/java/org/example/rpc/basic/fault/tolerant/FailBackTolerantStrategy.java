package org.example.rpc.basic.fault.tolerant;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.basic.model.RpcResponse;

import java.util.Map;


/**
 * 容错策略：服务降级
 * 遇到异常之后，调用降级的服务
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception exception) {
        // TODO
        return null;
    }
}
