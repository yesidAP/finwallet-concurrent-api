package com.finwallet.model;

import java.time.Instant;

/**
 * Represents the outcome of processing a transfer request.
 * <p>
 * This is a Java 21 sealed interface that permits only two possible results,
 * enabling exhaustive pattern matching in switch expressions.
 * This guarantees all case are handle at compile time.
 */
public sealed interface ProcessResult permits ProcessResult.Success, ProcessResult.Failed{

    /**
     * Successful transfer result.
     * @param transactionId Unique generated transaction ID
     * @param timestamp     Exact moment the transaction was completed.
     */
    record Success(String transactionId, Instant timestamp) implements ProcessResult {}

    /**
     * Failed transfer result.
     * @param reason    Human-readable description of the failure cause.
     * @param errorCode Internal code for tracing and metrics.
     */
    record Failed(String reason, String errorCode) implements ProcessResult {}
}

