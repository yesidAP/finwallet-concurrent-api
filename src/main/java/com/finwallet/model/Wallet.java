package com.finwallet.model;

import java.math.BigDecimal;

/**
 * Represents an immutable digital wallet in the finWallet system
 * <p>
 * Uses a java 21 record to ensure immutability and constructor validation.
 * The balance can never be negative according to business rules.
 *
 * @param id    Unique wallet identifier. Must be positive
 * @param owner Wallet owner name. Cannot be null or blank
 * @param balance Current balance. Cannot be null or negative.
 */
public record Wallet(Long id, String owner, BigDecimal balance) {

    public Wallet {
        if (id == null || id <= 0){
            throw new IllegalArgumentException("ID must be positive");
        }
        if (owner == null || owner.isBlank()){
            throw new IllegalArgumentException("Owner cannot be null or blank");
        }
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Balance cannot be negative");
        }

    }
}
