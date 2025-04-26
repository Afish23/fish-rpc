package com.fish.rpc.breaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Afish
 * @date 2025/4/26 15:25
 */
public class CircuitBreaker {
    private State state = State.CLOSED;
    private final AtomicInteger failCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger total = new AtomicInteger(0);
    //失败阈值
    private final int failThreshold;
    //成功阈值（半开状态下）
    private final double successRateInHalfOpen;
    //熔断时间窗口
    private final long windowTime;

    private long lastFailTime;

    public CircuitBreaker(int failThreshold, double successRateInHalfOpen, long windowTime) {
        this.failThreshold = failThreshold;
        this.successRateInHalfOpen = successRateInHalfOpen;
        this.windowTime = windowTime;
    }

    public synchronized boolean canReq() {
        switch (state) {
            case CLOSED:
                return true;
            case OPEN:
                if (System.currentTimeMillis() - lastFailTime <= windowTime) {
                    return false;
                }
                state = State.HALF_OPEN;
                resetCount();
                return true;
            case HALF_OPEN:
                total.incrementAndGet();
                return true;
            default:
                throw new IllegalStateException("熔断器状态异常");
        }
    }

    public synchronized void success() {
        if (state != State.HALF_OPEN) {
            resetCount();
        }
        successCount.incrementAndGet();
        if (successCount.get() >= successRateInHalfOpen * total.get()) {
            state = State.CLOSED;
            resetCount();
        }
    }

    public synchronized void fail() {
        failCount.incrementAndGet();
        lastFailTime = System.currentTimeMillis();
        if (state == State.HALF_OPEN) {
            state = State.OPEN;
            return;
        }
        if (failCount.get() >= failThreshold) {
            state = State.OPEN;
        }
    }

    private void resetCount(){
        total.set(0);
        successCount.set(0);
        failCount.set(0);
    }

    enum State {
        OPEN,
        CLOSED,
        HALF_OPEN,
    }
}
