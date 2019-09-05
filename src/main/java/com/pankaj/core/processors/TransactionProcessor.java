package com.pankaj.core.processors;

import com.pankaj.core.models.Transaction;
import com.pankaj.core.reporters.PositionReporter;
import com.pankaj.core.reporters.Reporter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum TransactionProcessor implements Processor {
    INSTANCE;

    private final ExecutorService executorService;

    TransactionProcessor() {
         executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static Processor getInstance() {
        return INSTANCE;
    }

    @Override
    public void process(Transaction txn) {
        executorService.execute(new TransactionCategorizationTask(txn));
    }

    @Override
    public Reporter getReporter() {
        return PositionReporter.getInstance();
    }
}
