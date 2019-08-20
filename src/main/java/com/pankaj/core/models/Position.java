package com.pankaj.core.models;

import lombok.Data;

@Data
public class Position {
    private final String sym;
    private final long value;
}