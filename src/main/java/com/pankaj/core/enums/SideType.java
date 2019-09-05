package com.pankaj.core.enums;

public enum SideType {
    BUY, SELL;

    public static SideType of(int i) {
        if (i == 2) {
            return SELL;
        }
        return BUY;
    }
}
