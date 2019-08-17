package com.pankaj.core.processors;

import com.pankaj.core.models.Transaction;
import com.pankaj.core.reporters.Reporter;

public interface Processor {
    void process(Transaction txn);
    Reporter getReporter();
}
