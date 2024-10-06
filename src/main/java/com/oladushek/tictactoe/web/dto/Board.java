package com.oladushek.tictactoe.web.dto;

import com.oladushek.tictactoe.service.model.Mark;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

@Data
@AllArgsConstructor
public class Board {
    private String[] board; //состояние доски
    private Mark currentPlayer;
    private String winner;

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
}
