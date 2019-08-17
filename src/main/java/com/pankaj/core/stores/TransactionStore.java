package com.pankaj.core.stores;

import com.pankaj.core.enums.TXN_STATUS;
import com.pankaj.core.models.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public enum TransactionStore {
    INSTANCE;

    private final EnumMap<TXN_STATUS, List<Transaction>> store = new EnumMap<>(TXN_STATUS.class);

    TransactionStore() {
        EnumSet.allOf(TXN_STATUS.class).forEach(status -> store.putIfAbsent(status, new ArrayList<>()));
    }

    public static TransactionStore getInstance() {
        return INSTANCE;
    }

    public List<Transaction> getTransactionsByStatus(TXN_STATUS status) {
        return Collections.unmodifiableList(store.get(status));
    }

    public List<Transaction> getTransactionOfOrdersByStatus(long orderId, TXN_STATUS status) {
        return getTransactionsByStatus(status).stream()
                .filter(txn -> txn.getOrderId() == orderId)
                .collect(Collectors.toList());
    }

    public boolean removeTransactionsOfStatus(List<Transaction> txns, TXN_STATUS status) {
        synchronized (store) {
            return store.get(status).removeAll(txns);
        }
    }

    public boolean putTransactionWithStatus(Transaction txn, TXN_STATUS status) {
        synchronized (store) {
            return store.get(status).add(txn);
        }
    }

    public void print() {
        store.values().forEach(System.out::println);
    }
}
