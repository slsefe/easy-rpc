package org.example.rpc.basic.fault.retrying;

import org.example.rpc.basic.model.RpcResponse;

import java.util.concurrent.Callable;

public interface RetryStrategy {

    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
