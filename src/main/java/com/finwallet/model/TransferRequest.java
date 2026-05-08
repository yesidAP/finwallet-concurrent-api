package com.finwallet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public record TransferRequest(
        @NotNull Long fromWalletId,
        @NotNull Long toWalletId,
        @NotNull @DecimalMin(value = "0.01", message = "Amount must be at least 0.01") BigDecimal amount) {}
