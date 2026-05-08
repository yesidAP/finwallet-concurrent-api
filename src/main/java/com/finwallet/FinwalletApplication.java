package com.finwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for FinWallet Concurrent API
 * Enables Spring Boot Auto-configuration and Virtual Threads support
 */
@SpringBootApplication
public class FinwalletApplication {
    public static void main(String[] args){
        SpringApplication.run(FinwalletApplication.class, args);
    }
}
