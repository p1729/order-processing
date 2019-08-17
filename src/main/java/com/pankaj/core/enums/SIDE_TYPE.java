package com.pankaj.core.enums;

public enum SIDE_TYPE {
    BUY, SELL;

    public static SIDE_TYPE of(int i) {
        switch(i) {
            case 2: return SELL;
            default: return BUY;
        }
    }
}
