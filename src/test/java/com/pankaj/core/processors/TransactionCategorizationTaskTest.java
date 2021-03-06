package com.pankaj.core.processors;

import com.pankaj.core.MockData;
import com.pankaj.core.enums.TransactionStatus;
import com.pankaj.core.models.Transaction;
import com.pankaj.core.reporters.PositionReporter;
import com.pankaj.core.rules.RulesStore;
import com.pankaj.core.stores.OrderStore;
import com.pankaj.core.stores.TransactionStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderStore.class, TransactionStore.class, RulesStore.class, Executors.class, PositionReporter.class})
public class TransactionCategorizationTaskTest {

    @Captor
    ArgumentCaptor<Transaction> txnCaptor;

    @Captor
    ArgumentCaptor<TransactionStatus> txnStatusCaptor;

    @Before
    public void setup() {
        mockStatic(PositionReporter.class);
        PositionReporter rptMock = mock(PositionReporter.class);
        Whitebox.setInternalState(PositionReporter.class, "INSTANCE", rptMock);
        when(PositionReporter.getInstance()).thenReturn(rptMock);
        doAnswer(o -> null).when(rptMock).prepare();
    }

    @Test
    public void givenTxn_whenPendingRuleApplied_thenItsNotProcessed() throws NoSuchFieldException, IllegalAccessException {
        //Given
        mockStatic(RulesStore.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> true);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> false);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(List.of(MockData.cancelTransactionV2O1));
        TransactionCategorizationTask task = new TransactionCategorizationTask(MockData.insertTransactionV1O1);

        Field store = TransactionCategorizationTask.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(task, txnMock);
        Field orders = TransactionCategorizationTask.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(task, orderMock);
        //when
        task.run();
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TransactionStatus.PENDING);
    }

    @Test
    public void givenTxn_whenProcessRuleApplied_thenItsProcessed() throws NoSuchFieldException, IllegalAccessException {
        //given
        mockStatic(RulesStore.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> true);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(Collections.emptyList());

        TransactionCategorizationTask task = new TransactionCategorizationTask(MockData.insertTransactionV1O1);

        Field store = TransactionCategorizationTask.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(task, txnMock);
        Field orders = TransactionCategorizationTask.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(task, orderMock);
        //when
        task.run();
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TransactionStatus.PROCESSED);
    }

    @Test
    public void givenTxn_whenRejectRuleApplied_thenItsRejected() throws NoSuchFieldException, IllegalAccessException {
        //given
        mockStatic(RulesStore.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> true);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> false);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(Collections.emptyList());

        TransactionCategorizationTask task = new TransactionCategorizationTask(MockData.insertTransactionV1O1);
        Field store = TransactionCategorizationTask.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(task, txnMock);
        Field orders = TransactionCategorizationTask.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(task, orderMock);
        //when
        task.run();
        //then
        verify(txnMock, times(1)).putTransactionWithStatus(MockData.insertTransactionV1O1, TransactionStatus.REJECTED);
    }

    @Test
    public void givenTxn_whenProcessRuleAppliedAndPendingTransactionPresent_thenBothTransactionProcessed() throws NoSuchFieldException, IllegalAccessException {

        //given
        mockStatic(RulesStore.class);
        OrderStore orderMock = mock(OrderStore.class);
        TransactionStore txnMock = mock(TransactionStore.class);
        Whitebox.setInternalState(OrderStore.class, "INSTANCE", orderMock);
        Whitebox.setInternalState(TransactionStore.class, "INSTANCE", txnMock);
        when(orderMock.getOrderById(anyLong())).thenReturn(Optional.empty());
        when(RulesStore.getPendingTransactionOrderRule()).thenReturn((a,b) -> false);
        when(RulesStore.getRejectedTransactionRule()).thenReturn((a,b) -> false);
        when(RulesStore.getTransactionOrderProcessingRule()).thenReturn((a,b) -> true);
        when(txnMock.getTransactionOfOrdersByStatus(anyLong(), anyObject())).thenReturn(MockData.listofOrderVersion, Collections.emptyList());

        TransactionCategorizationTask task = new TransactionCategorizationTask(MockData.insertTransactionV1O1);
        Field store = TransactionCategorizationTask.class.getDeclaredField("store");
        store.setAccessible(true);
        store.set(task, txnMock);
        Field orders = TransactionCategorizationTask.class.getDeclaredField("orders");
        orders.setAccessible(true);
        orders.set(task, orderMock);
        //when
        task.run();
        //then
        verify(txnMock, times(2)).putTransactionWithStatus(txnCaptor.capture(), txnStatusCaptor.capture());
        Assert.assertEquals(txnCaptor.getAllValues().get(0), MockData.insertTransactionV1O1);
        Assert.assertEquals(txnCaptor.getAllValues().get(1), MockData.updateTransactionV2O1);
        Assert.assertEquals(TransactionStatus.PROCESSED, txnStatusCaptor.getAllValues().get(0));
        Assert.assertEquals(TransactionStatus.PROCESSED, txnStatusCaptor.getAllValues().get(1));
    }

}