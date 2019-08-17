package com.pankaj.core.enums;

public enum TXN_TYPE {
    INSERT, UPDATE, CANCEL;

    public static TXN_TYPE of(int i) {
        switch(i) {
            case 2: return UPDATE;
            case 3: return CANCEL;
            default: return INSERT;
        }
    }
}
