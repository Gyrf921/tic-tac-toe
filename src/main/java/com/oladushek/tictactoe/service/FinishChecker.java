package com.oladushek.tictactoe.service;

import java.util.Arrays;


public class FinishChecker {

    public static boolean checkDraw(String[] board){
        return Arrays.stream(board).noneMatch(String::isEmpty);
    }

    public static boolean checkWinner(String[] board, String player) {
        String[][] winningCombinations = {
                {board[0], board[1], board[2]},
                {board[3], board[4], board[5]},
                {board[6], board[7], board[8]},
                {board[0], board[3], board[6]},
                {board[1], board[4], board[7]},
                {board[2], board[5], board[8]},
                {board[0], board[4], board[8]},
                {board[2], board[4], board[6]}
        };

        return Arrays.stream(winningCombinations)
                .anyMatch(combination -> combination[0] != null &&
                        combination[0].equals(player) &&
                        combination[1].equals(player) &&
                        combination[2].equals(player));
    }

}
