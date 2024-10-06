package com.oladushek.tictactoe.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Mark {

    X("X"),
    O("O"),
    Z("");

    private final String value;


}
