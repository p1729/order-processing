package com.pankaj.core.stores;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public enum OrderStore implements Iterable<Order> {
    INSTANCE;

    private final List<Order> store = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static OrderStore getInstance() {
        return INSTANCE;
    }

    public Optional<Order> getOrderById(long orderId) {
        readWriteLock.readLock().lock();
        Optional<Order> order = store.stream().filter(o -> o.getOrderId() == orderId).findFirst();
        readWriteLock.readLock().unlock();
        return order;
    }

    public void addOrder(Order order) {
        readWriteLock.writeLock().lock();
        store.add(order);
        readWriteLock.writeLock().unlock();
    }

    public boolean addOrderVersion(long orderId, OrderVersion orderVersion) {
        Optional<Order> orderById = getOrderById(orderId);
        readWriteLock.writeLock().lock();
        Boolean isVersionAdded = orderById.map(order -> order.getVersions().add(orderVersion)).orElse(false);
        readWriteLock.writeLock().unlock();
        return isVersionAdded;
    }

    @Override
    public Iterator<Order> iterator() {
        readWriteLock.readLock().lock();
        Iterator<Order> iterator = Collections.unmodifiableList(store).iterator();
        readWriteLock.readLock().unlock();
        return iterator;
    }

    public List<Order> getStore() {
        readWriteLock.readLock().lock();
        List<Order> orders = new ArrayList<>();
        store.forEach(o -> {
            orders.add(Order.shallowCopyOfAllLists(o));
        });
        readWriteLock.readLock().unlock();
        return orders;
    }
}