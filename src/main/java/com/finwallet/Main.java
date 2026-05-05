package com.finwallet;

import com.finwallet.model.ProcessResult;
import com.finwallet.model.Wallet;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args){
        Wallet alice = new Wallet(1L, "Alice", new BigDecimal("1000.00"));
        Wallet bob = new Wallet(2L, "Bob", new BigDecimal("500.00"));

        System.out.println("Alice: " + alice);
        System.out.println("Bob:" + bob);

        ProcessResult result = new ProcessResult.Success("TX-001 ", java.time.Instant.now());

        String message = switch (result){
            case ProcessResult.Success s -> "Success " + s.transactionId() + "at " + s.timestamp();
            case ProcessResult.Failed f ->  "Failed " + f.reason() + "error code: " + f.errorCode();
        };

        System.out.println(message);
    }
}
