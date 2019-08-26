package com.pankaj.core.enums;

public enum TransactionType {
    INSERT, UPDATE, CANCEL;

    public static TransactionType of(int i) {
        switch(i) {
            case 2: return UPDATE;
            case 3: return CANCEL;
            default: return INSERT;
        }
    }
}
