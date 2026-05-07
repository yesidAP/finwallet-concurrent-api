package com.finwallet;

import com.finwallet.model.ProcessResult;
import com.finwallet.model.TransferRequest;
import com.finwallet.model.Wallet;
import com.finwallet.service.FraudCheckService;
import com.finwallet.service.WalletService;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args){

        try(FraudCheckService fraudService = new FraudCheckService()) {
            WalletService walletService = new WalletService(fraudService);
            int totalTransfers = 10_000;
            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();

            System.out.println("Starting " + totalTransfers + " concurrent transfers...");
            Instant start = Instant.now();

            //Java 21: Virtual Threads
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                IntStream.range(0, totalTransfers).forEach(i ->
                        executor.submit(() -> {
                            TransferRequest req = new TransferRequest(1L, 2L, new BigDecimal("50"));
                            ProcessResult result = walletService.transfer(req).join();

                            switch (result) {
                                case ProcessResult.Success s -> successCount.incrementAndGet();
                                case ProcessResult.Failed f -> failCount.incrementAndGet();
                            }

                        })
                );
            } //Try-with-resources closes the executor and wait for all tasks finish.
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            System.out.println("\n === Results ===");
            System.out.println("Total time: " + duration.toMillis() + "ms");
            System.out.println("Success: " + successCount.get());
            System.out.println("Failed: " + failCount.get());
            System.out.println("Alice final balance: " + walletService.getWallet(1L).balance());
            System.out.println("Bob final balance: " + walletService.getWallet(2L).balance());

            //Validation: 1000 - (10000 * 0.10) = 0.00 | 500 + (10000 * 0.10) = 1500.00
            BigDecimal expectedAlice = new BigDecimal("0.00");
            BigDecimal expectedBob = new BigDecimal("1500.00");

            if (walletService.getWallet(1L).balance().compareTo(expectedAlice) == 0
                    && walletService.getWallet(2L).balance().compareTo(expectedBob) == 0) {
                System.out.println("No race conditions detected. Balance are correct!");
            } else {
                System.out.println("RACE CONDITION DETECTED! Data corruption occurred");
            }
        }


    }
}
