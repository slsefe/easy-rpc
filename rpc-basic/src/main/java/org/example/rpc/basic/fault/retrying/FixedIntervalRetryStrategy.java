package org.example.rpc.basic.fault.retrying;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.basic.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试策略：固定时间间隔
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                // 重试条件：指定发生的异常类型
                .retryIfExceptionOfType(Exception.class)
                // 重试等待策略：每次重试间隔3秒
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                // 重试停止策略：最多重试3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                // 每次重试时：额外打印重试次数
                .withRetryListener(new RetryListener() {

                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数: {}", attempt.getAttemptNumber());
                    }
                }).build();
        return retryer.call(callable);
    }
}
