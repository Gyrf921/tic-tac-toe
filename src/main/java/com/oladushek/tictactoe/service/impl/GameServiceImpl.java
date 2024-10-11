package com.oladushek.tictactoe.service.impl;

import com.oladushek.tictactoe.service.alg.FinishChecker;
import com.oladushek.tictactoe.service.GameService;
import com.oladushek.tictactoe.service.alg.MinimaxAlg;
import com.oladushek.tictactoe.web.dto.game.Board;
import com.oladushek.tictactoe.web.dto.game.GameMove;
import com.oladushek.tictactoe.web.dto.game.Mark;
import com.oladushek.tictactoe.web.dto.game.OpponentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final Map<UUID, Board> sessions = new HashMap<>();

    private final MinimaxAlg minimaxAlg;

    @Override
    public Board play(UUID sessionId, OpponentType opponentType, GameMove move) {

        Board state = getState(sessionId, opponentType, move);

        if (state.getBoard()[move.getIndex()].isEmpty()) {
            state.getBoard()[move.getIndex()] = move.getPlayer();

            if (FinishChecker.checkWinner(state.getBoard(), move.getPlayer())) {
                state.setWinner(move.getPlayer()); // Устанавливаем победителя
                return state;
            }

            state.setCurrentPlayer(move.getPlayer().equals("X") ? Mark.O : Mark.X);

            if (OpponentType.isComputer(opponentType)){
                return computerMove(state);
            }
        }

        return state;
    }

    @Override
    public Board clear(UUID sessionId) {
        return sessions.get(sessionId).clear();
    }

    private Board getState(UUID sessionId, OpponentType opponentType, GameMove move){
        if(!sessions.containsKey(sessionId)) {
            Board board = new Board().toBuilder()
                    .withPerson(!OpponentType.isComputer(opponentType))
                    .currentPlayer(Mark.valueOf(move.getPlayer())).build();
            sessions.put(sessionId, board);
        }
        return sessions.get(sessionId);
    }

    private Board computerMove(Board state){
        GameMove gameMove = minimaxAlg.getBestMove(state);
        state.getBoard()[gameMove.getIndex()] = gameMove.getPlayer();
        if (FinishChecker.checkWinner(state.getBoard(), gameMove.getPlayer())) {
            state.setWinner(gameMove.getPlayer()); // Устанавливаем победителя
        }
        state.setCurrentPlayer(gameMove.getPlayer().equals("X") ? Mark.O : Mark.X);
        return state;
    }

}
