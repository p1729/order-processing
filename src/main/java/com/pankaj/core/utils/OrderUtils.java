package com.pankaj.core.utils;

import com.pankaj.core.enums.TransactionType;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.List.of;

public class OrderUtils {

    public static boolean isNotFirstOrderVersion(Transaction txn) {
        return OrderVersion.FIRST_ORDER_VERSION != txn.getVersionId();
    }

    public static boolean isOrderCanceled(Order processedOrder) {
        if(Objects.isNull(processedOrder)) return false;
        return processedOrder.getVersionsForRead().stream()
                .anyMatch(ver -> ver.getTxnType().equals(TransactionType.CANCEL));
    }

    public static long getNextOrderVersionInSequence(Order order) {
        if(Objects.isNull(order)) return OrderVersion.FIRST_ORDER_VERSION;
        List<OrderVersion> sortedVers = order.getVersionsForRead()
                .stream().sorted().collect(Collectors.toList());
        return sortedVers.get(sortedVers.size()-1).getVersionId() + 1;
    }

    public static <T> List<T> copyOfList(List<T> list) {
        return (List<T>) of(list.toArray());
    }

    public static <T> Stream<T> getStreamFromIterator(Iterator<T> iterator) {
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }
}
