package com.oladushek.tictactoe.service;

import com.oladushek.tictactoe.web.dto.GameMove;
import com.oladushek.tictactoe.web.dto.GameState;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@Getter
public class GameService {

    private GameState state;

    public GameService() {
        this.state = new GameState();
    }
    public GameState play(GameMove move) {
        state.setCurrentPlayer(move.getPlayer());
        if (Objects.equals(state.getBoard()[move.getIndex()], "")) {
            state.getBoard()[move.getIndex()] = move.getPlayer();
            if (checkWinner(move.getPlayer())) {
                state.setWinner(move.getPlayer()); // Устанавливаем победителя
            }
            else {
                // Меняем текущего игрока
                state.setCurrentPlayer(state.getCurrentPlayer().equals("X") ? "O" : "X");
            }
        }
        return state;
    }

    private boolean checkWinner(String player) {
        String[] board = state.getBoard();
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

