package com.oladushek.tictactoe.web.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Board {
    private String[] board; //состояние доски
    private Mark currentPlayer;
    private String winner;
    private boolean withPerson;

    public Board() {
        this.board = new String[]{"", "", "", "", "", "", "", "", ""};
    }

    public void setMoveToBoard(Integer index, Mark mark) {
        this.board[index] = mark.getValue();
    }

    public boolean isTileMarked(Integer index){
        return !board[index].isEmpty();
    }

    public Integer getCountEmptyCell(){
        return (int) Arrays.stream(board).filter(String::isEmpty).count();
    }

    public Board clear() {
        this.board = new String[]{"", "", "", "", "", "", "", "", ""};
        this.winner = null;
        this.currentPlayer = Mark.X;
        return this;
    }
}
