package com.pankaj.core.stores;

import com.pankaj.core.enums.TXN_STATUS;
import com.pankaj.core.models.Transaction;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public enum TransactionStore {
    INSTANCE;

    private final EnumMap<TXN_STATUS, List<Transaction>> store = new EnumMap<>(TXN_STATUS.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    TransactionStore() {
        EnumSet.allOf(TXN_STATUS.class).forEach(status -> store.putIfAbsent(status, new ArrayList<>()));
    }

    public static TransactionStore getInstance() {
        return INSTANCE;
    }

    public List<Transaction> getTransactionsByStatus(TXN_STATUS status) {
        readWriteLock.readLock().lock();
        List<Transaction> transactions = Collections.unmodifiableList(store.get(status));
        readWriteLock.readLock().unlock();
        return transactions;
    }

    public List<Transaction> getTransactionOfOrdersByStatus(long orderId, TXN_STATUS status) {
        readWriteLock.readLock().lock();
        List<Transaction> transactions = getTransactionsByStatus(status).stream()
                .filter(txn -> txn.getOrderId() == orderId)
                .collect(Collectors.toList());
        readWriteLock.readLock().unlock();
        return transactions;
    }

    public boolean removeTransactionsOfStatus(List<Transaction> txns, TXN_STATUS status) {
        readWriteLock.writeLock().lock();
        boolean isRemoved = store.get(status).removeAll(txns);
        readWriteLock.writeLock().unlock();
        return isRemoved;
    }

    public void putTransactionWithStatus(Transaction txn, TXN_STATUS status) {
        readWriteLock.writeLock().lock();
        store.get(status).add(txn);
        readWriteLock.writeLock().unlock();
    }
}
