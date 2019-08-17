package com.pankaj.core.stores;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;

import java.util.*;

public enum OrderStore implements Iterable<Order> {
    INSTANCE;

    private final List<Order> store = new ArrayList<>();

    public static OrderStore getInstance() {
        return INSTANCE;
    }

    public Optional<Order> getOrderById(long orderId) {
        synchronized (store) {
            return store.stream().filter(o -> o.getOrderId() == orderId).findFirst();
        }
    }

    public boolean addOrder(Order order) {
        synchronized (store) {
            return store.add(order);
        }
    }

    public boolean addOrderVersion(long orderId, OrderVersion orderVersion) {
        synchronized (store) {
            return getOrderById(orderId).map(order -> order.getVersions().add(orderVersion)).orElse(false);
        }
    }

    public void print() {
        synchronized (store) {
            store.forEach(System.out::println);
        }
    }

    @Override
    public Iterator<Order> iterator() {
        synchronized (store) {
            return Collections.unmodifiableList(store).iterator();
        }
    }

    public List<Order> getStore() {
        return store;
    }
}