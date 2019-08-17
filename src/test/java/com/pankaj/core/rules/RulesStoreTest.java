package com.pankaj.core.rules;

import com.pankaj.core.MockData;
import com.pankaj.core.models.Order;
import com.pankaj.core.models.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RulesStoreTest {

    @Test
    void givenNoOrderWhenInsertTxnO1V1ThenTxnProcessed() {
        Order order = null;
        Transaction txn = MockData.insertTransactionV1O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenNoOrderWhenUpdateTxnO1V1ThenTxnGetRejected() {
        Order order = null;
        Transaction txn = MockData.updateTransactionV1O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenNoOrderWhenCancelTxnO1V1ThenTxnGetRejected() {
        Order order = null;
        Transaction txn = MockData.cancelTransactionV1O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenNoOrderWhenUpdateTxnO1V2ThenTxnPending() {
        Order order = null;
        Transaction txn = MockData.updateTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenNoOrderWhenCancelTxnO1V2ThenTxnPending() {
        Order order = null;
        Transaction txn = MockData.cancelTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenNoOrderWhenInsertTxnO1V2ThenTxnRejected() {
        Order order = null;
        Transaction txn = MockData.insertTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithInsertWhenUpdateThenTxnProcessed() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.updateTransactionV2O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithInsertWhenCancelThenTxnProcessed() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.cancelTransactionV2O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithInsertWhenInsertThenTxnRejected() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.insertTransactionV2O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithInsertWhenUpdateTxnO1V3ThenTxnPending() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.updateTransactionV3O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithInsertWhenCancelTxnO1V3ThenTxnPending() {
        Order order = MockData.orderWithInsert;
        Transaction txn = MockData.cancelTransactionV3O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertTrue(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithUpdateWhenUpdateTxnO1V3ThenTxnProcessed() {
        Order order = MockData.orderWithUpdate;
        Transaction txn = MockData.updateTransactionV3O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithUpdateWhenCancelTxnO1V3ThenTxnProcessed() {
        Order order = MockData.orderWithUpdate;
        Transaction txn = MockData.cancelTransactionV3O1;

        assertTrue(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertFalse(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithCancelWhenCancelTxnO1V4ThenTxnRejected() {
        Order order = MockData.orderWithCancel;
        Transaction txn = MockData.cancelTransactionV4O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }

    @Test
    void givenOrderWithCancelWhenUpdateTxnO1V4ThenTxnRejected() {
        Order order = MockData.orderWithCancel;
        Transaction txn = MockData.updateTransactionV4O1;

        assertFalse(RulesStore.getTransactionOrderProcessingRule().apply(txn, order));
        assertFalse(RulesStore.getPendingTransactionOrderRule().apply(txn, order));
        assertTrue(RulesStore.getRejectedTransactionRule().apply(txn, order));
    }
}