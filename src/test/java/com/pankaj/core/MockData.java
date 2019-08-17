package com.pankaj.core;

import com.pankaj.core.models.Order;
import com.pankaj.core.models.OrderVersion;
import com.pankaj.core.models.Transaction;

import java.util.List;

import static com.pankaj.core.enums.SIDE_TYPE.*;
import static com.pankaj.core.enums.TXN_TYPE.*;
import static java.util.List.of;

public class MockData {
    public static Order orderWithInsert = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY)
    ));

    public static Order orderWithUpdate = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
            new OrderVersion(2L, "ABC", 1000L, UPDATE, BUY)
    ));

    public static Order orderWithCancel = new Order(1L, of(
            new OrderVersion(1L, "ABC", 1000L, INSERT, BUY),
            new OrderVersion(2L, "ABC", 1000L, CANCEL, BUY)
    ));

    public static Transaction insertTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, INSERT, BUY);

    public static Transaction insertTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, INSERT, BUY);

    public static Transaction updateTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV3O1 =
            new Transaction(1L, 1L, 3L, "ABC", 10L, UPDATE, BUY);

    public static Transaction updateTransactionV4O1 =
            new Transaction(1L, 1L, 4L, "ABC", 10L, UPDATE, BUY);

    public static Transaction cancelTransactionV1O1 =
            new Transaction(1L, 1L, 1L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV2O1 =
            new Transaction(1L, 1L, 2L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV3O1 =
            new Transaction(1L, 1L, 3L, "ABC", 10L, CANCEL, BUY);

    public static Transaction cancelTransactionV4O1 =
            new Transaction(1L, 1L, 4L, "ABC", 10L, CANCEL, BUY);

    public static List<Transaction> txns = List.of(
            new Transaction(1L, 1L, 1L, "RELIANCE", 1000L, INSERT, BUY),
            new Transaction(2L, 2L, 1L, "TCS", 500L, INSERT, SELL),
            new Transaction(3L, 3L, 2L, "INFOSYS", 1000L, UPDATE, BUY),
            new Transaction(8L, 7L, 1L, "AAAA", 500L, CANCEL, BUY),
            new Transaction(4L, 4L, 1L, "RELIANCE", 500L, INSERT, SELL),
            new Transaction(5L, 3L, 1L, "INFOSYS", 800L, INSERT, BUY),
            new Transaction(9L, 8L, 1L, "BBBB", 500L, CANCEL, BUY),
            new Transaction(6L, 5L, 2L, "IBM", 500L, CANCEL, BUY),
            new Transaction(7L, 5L, 1L, "IBM", 500L, INSERT, BUY)
    );
}
