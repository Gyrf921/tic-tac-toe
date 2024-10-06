package com.oladushek.tictactoe.service;

import com.oladushek.tictactoe.service.model.Mark;
import com.oladushek.tictactoe.web.dto.GameMove;
import com.oladushek.tictactoe.web.dto.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Getter
@NoArgsConstructor
public class GamePvPService {

    public Board play(GameMove move, Board state) {
        state.setCurrentPlayer(Mark.valueOf(move.getPlayer()));

        if (Objects.equals(state.getBoard()[move.getIndex()], "")) {
            state.getBoard()[move.getIndex()] = move.getPlayer();

            if (FinishChecker.checkWinner(state.getBoard(), move.getPlayer())) {
                state.setWinner(move.getPlayer()); // Устанавливаем победителя
            }
            else {
                state.setCurrentPlayer(move.getPlayer().equals("X") ? Mark.O : Mark.X);
            }
        }
        return state;
    }
}

