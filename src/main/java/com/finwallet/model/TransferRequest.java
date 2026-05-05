package com.finwallet.model;

import java.math.BigDecimal;

/**
 * Immutable request to transfer funds between two wallets.
 * <p>
 * Validate business rules in the compact constructor to ensure the
 * request is valid before processing.
 *
 * @param fromWalletId  Source wallet ID.
 * @param toWalletId    Target wallet ID. Cannot be the same as the source.
 * @param amount        Amount to transfer. Must be positive.
 */
public record TransferRequest(Long fromWalletId, long toWalletId, BigDecimal amount) {
    public TransferRequest{
        if (fromWalletId.equals(toWalletId)){
            throw new IllegalArgumentException("You cannot transfer funds to the same wallet");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 ){
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
