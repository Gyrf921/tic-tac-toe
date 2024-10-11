package com.oladushek.tictactoe.web.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.plaf.PanelUI;

@Getter
@AllArgsConstructor
public enum OpponentType {
    PERSON( "person"),
    COMPUTER( "computer");

    private final String name;

    public static boolean isComputer(OpponentType opponentType){
        return opponentType == COMPUTER;
    }
}
