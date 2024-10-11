package com.oladushek.tictactoe.web.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Mark {

    X("X"),
    O("O"),
    Z("");

    private final String value;
}
