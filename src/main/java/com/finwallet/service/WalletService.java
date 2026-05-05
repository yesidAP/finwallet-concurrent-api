package com.finwallet.service;

import com.finwallet.model.ProcessResult;
import com.finwallet.model.TransferRequest;
import com.finwallet.model.Wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe service for wallet operations using Virtual Threads.
 * <p>
 * Uses {@link java.util.concurrent.locks.ReentrantLock} per wallet to prevent
 * race conditions during concurrent transfer. Locks are acquired in consistent order to prevent deadlocks.
 */
public class WalletService {

    private final Map<Long, Wallet> wallets = new ConcurrentHashMap<>();
    private final Map<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public WalletService(){
        //Seed data: Alice 1000, Bob 500
        wallets.put(1L, new Wallet(1L, "Alice", new BigDecimal("1000")));
        wallets.put(2L, new Wallet(2L, "Bob", new BigDecimal("500")));
    }

    /**
     * Processes a transfer between two wallets atomically
     * DeadLock prevention: always lock lower ID first
     */
    public ProcessResult transfer(TransferRequest request){
        Long fromId = request.fromWalletId();
        Long toId   = request.toWalletId();

        //Lock ordering: avoid deadlock A -> B and B -> A
        Long firstLock  = Math.min(fromId, toId);
        Long secondLock = Math.max(fromId, toId);

        ReentrantLock lock1 = locks.computeIfAbsent(firstLock, k -> new ReentrantLock());
        ReentrantLock lock2 = locks.computeIfAbsent(secondLock, k -> new ReentrantLock());

        lock1.lock();
        try{
            lock2.lock();

            try {
                Wallet from = wallets.get(fromId);
                Wallet to = wallets.get(toId);

                if (from == null || to == null){
                    return new ProcessResult.Failed("Wallet not found", "WALLET_404");
                }

                if(from.balance().compareTo(request.amount()) < 0){
                    return new ProcessResult.Failed("Insufficient funds", "INSUFFICIENT_FUNDS");

                }

                Wallet newFrom = new Wallet(from.id(), from.owner(), from.balance().subtract(request.amount()));
                Wallet newTo = new Wallet(to.id(), to.owner(), to.balance().add(request.amount()));

                wallets.put(fromId, newFrom);
                wallets.put(toId, newTo);

                return new ProcessResult.Success(UUID.randomUUID().toString(), Instant.now());

            }finally {
                lock2.unlock();
            }
        }finally {
            lock1.unlock();
        }


    }

    public Wallet getWallet(Long id){
        return wallets.get(id);
    }
}
