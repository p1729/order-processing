package com.pankaj.core.enums;

public enum SideType {
    BUY, SELL;

    public static SideType of(int i) {
        switch(i) {
            case 2: return SELL;
            default: return BUY;
        }
    }
}
