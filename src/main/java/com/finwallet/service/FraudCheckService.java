package com.finwallet.service;

import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * Service responsible for validating transfer against fraud detection engine.
 * <p>
 * This simulates an external microservices call with network latency.
 * uses {@link CompletableFuture} to avoid blocking threads during I/O operations
 * In production, this would call an external REST/gRPC fraud service
 */
public class FraudCheckService implements AutoCloseable{

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());
    /**
     * Asynchronously validates if a transfer should be allowed based on fraud rules.
     * <p>
     * This method does not block the calling thread. The 200ms delay simulates
     * network + processing time of a real fraud engine.
     *
     * @param fromId    the source wallet ID
     * @param toId      toID the destination wallet ID
     * @param amount    amount the transfer amount to validate
     * @return a {@link CompletableFuture} that complete with {@code true}
     * if the transfer is approved, or {@code false} if reject by fraud rules.
     */
    public CompletableFuture<Boolean> validateTransfer(Long fromId, Long toId, BigDecimal amount){
        CompletableFuture<Boolean> result = new CompletableFuture<>();

        scheduler.schedule( () -> {
            if (amount.compareTo(new BigDecimal("50")) > 0) {
                result.complete(ThreadLocalRandom.current().nextInt(20) != 0);
            } else {
                result.complete(true);
            }
            }, 200, TimeUnit.MILLISECONDS);
        return result;

    }

    @Override
    public void close(){
        scheduler.shutdown();
    }
}
