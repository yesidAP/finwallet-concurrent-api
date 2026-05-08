package com.finwallet.controller;

import com.finwallet.model.ProcessResult;
import com.finwallet.model.TransferRequest;
import com.finwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * REST controller exposing wallet transfer operations
 * <p>
 * All endpoints are fully non-blocking and executed on Virtual Threads.
 * The underlying {@link WalletService}guarantees zero races conditions via
 * fine-grained locking and prevents deadlocks with ordered locks acquisition.
 */
@RestController
@RequestMapping("api/v1")
public class TransferController {
    private final WalletService walletService;

    /**
     * Constructs the controller with requires dependencies.
     * @param walletService the core transfer service, injected by Spring
     */
    public TransferController(WalletService walletService){
        this.walletService = walletService;
    }

    /**
     * Executes a wallet-to-wallet transfer asynchronously.
     * <p>
     * Includes fraud validation with 200ms simulated latency. For amounts > 50,
     * the system has a 5% rejection rate. Insufficient founds or fraud detection
     * will result in a {@code 400 Bad request}.
     *
     * @param request transfer details with source, destination and amount. Must be valid.
     * @return {@link CompletableFuture} containing {@link ResponseEntity} with the transfer result.
     *         Return {@code 200 OK} on success, {@code 400 Bad Request} on failure
     */
    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<ProcessResult>> transfer(@Valid @RequestBody TransferRequest request){
        return walletService.transfer(request)
                .thenApply(result ->
                        switch (result) {
                            case ProcessResult.Success s -> ResponseEntity.ok(result);
                            case ProcessResult.Failed f ->  ResponseEntity.badRequest().body(result);

                        });
    }
}
