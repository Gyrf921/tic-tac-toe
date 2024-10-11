package com.oladushek.tictactoe.web.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameMove {
    private int index;
    private String player;
}
