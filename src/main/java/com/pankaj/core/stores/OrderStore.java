package com.pankaj.core.stores;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.utils.OrderUtils;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.pankaj.core.models.Order.shallowCopyOfAllLists;

public enum OrderStore implements Iterable<Order> {
    INSTANCE;

    private final List<Order> store = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static OrderStore getInstance() {
        return INSTANCE;
    }

    public Optional<Order> getOrderById(long orderId) {
        return getOrderByIdForWrite(orderId).map(Order::shallowCopyOfAllLists);
    }

    public void addOrder(Order order) {
        readWriteLock.writeLock().lock();
        store.add(order);
        readWriteLock.writeLock().unlock();
    }

    public boolean addOrderVersion(long orderId, OrderVersion orderVersion) {
        Optional<Order> orderById = getOrderByIdForWrite(orderId);
        readWriteLock.writeLock().lock();
        Boolean isVersionAdded = orderById.map(order -> order.getVersions().add(orderVersion)).orElse(false);
        readWriteLock.writeLock().unlock();
        return isVersionAdded;
    }

    @Override
    public Iterator<Order> iterator() {
        List<Order> orders = new ArrayList<>();
        readWriteLock.readLock().lock();
        store.forEach(o -> orders.add(shallowCopyOfAllLists(o)));
        readWriteLock.readLock().unlock();
        return orders.iterator();
    }

    private Optional<Order> getOrderByIdForWrite(long orderId) {
        readWriteLock.readLock().lock();
        Optional<Order> order = store.stream().filter(o -> o.getOrderId() == orderId).findFirst();
        readWriteLock.readLock().unlock();
        return order;
    }
}