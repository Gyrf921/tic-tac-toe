package com.oladushek.tictactoe.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
public class GameState {
    private String[] board; // состояние доски
    private String currentPlayer;
    private String winner;

    public GameState() {
        this.board = new String[]{"", "", "", "", "", "", "", "", ""};

    }
}
