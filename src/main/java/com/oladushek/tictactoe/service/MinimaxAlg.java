package com.oladushek.tictactoe.service;


import com.oladushek.tictactoe.service.model.Mark;
import com.oladushek.tictactoe.web.dto.GameMove;
import com.oladushek.tictactoe.web.dto.Board;
import org.springframework.stereotype.Component;

@Component
public class MinimaxAlg {


    public GameMove getBestMove(Board board){
        GameMove bestMove = new GameMove();
        board.setCurrentPlayer(board.getCurrentPlayer());
        bestMove.setPlayer(board.getCurrentPlayer().getValue());
        int bestValue = Integer.MAX_VALUE;

        for(int i = 0; i < board.getBoard().length; i++){
            if (!board.isTileMarked(i)) {
                board.setMoveToBoard(i, board.getCurrentPlayer());
                int moveValue = minmax(board);
                board.setMoveToBoard(i, Mark.Z);
                if (moveValue < bestValue){
                    bestMove.setIndex(i);
                    bestValue = moveValue;
                }
            }
        }

        return bestMove;
    }

    private int minmax(Board board) {

        if (FinishChecker.checkWinner(board.getBoard(), board.getCurrentPlayer().getValue())) {
            if (board.getCurrentPlayer() == Mark.X){
                return 11 + board.getCountEmptyCell();
            }
            else if (board.getCurrentPlayer()  == Mark.O){
                return -10 - board.getCountEmptyCell();
            }
        } else if (FinishChecker.checkDraw(board.getBoard())) {
            return 0;
        }

        if (board.getCurrentPlayer().equals(Mark.X)) {
            int maxScope = Integer.MIN_VALUE;
            for (int i = 0; i < board.getBoard().length; i++) {
                if (!board.isTileMarked(i)) {
                    board.setMoveToBoard(i, board.getCurrentPlayer());
                    board.setCurrentPlayer(Mark.O);
                    maxScope = Math.max(minmax(board), maxScope);
                    board.setCurrentPlayer(Mark.X);
                    board.setMoveToBoard(i, Mark.Z);
                }
            }
            return maxScope;
        }
        else {
            int minScope = Integer.MAX_VALUE;
            for (int i = 0; i < board.getBoard().length; i++) {
                if (!board.isTileMarked(i)) {
                    board.setMoveToBoard(i, board.getCurrentPlayer());
                    board.setCurrentPlayer(Mark.X);
                    minScope = Math.min(minmax(board), minScope);
                    board.setCurrentPlayer(Mark.O);
                    board.setMoveToBoard(i, Mark.Z);
                }
            }
            return minScope;
        }
    }
}
