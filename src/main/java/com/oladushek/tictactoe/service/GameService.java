package com.oladushek.tictactoe.service;

import com.oladushek.tictactoe.web.dto.game.Board;
import com.oladushek.tictactoe.web.dto.game.GameMove;
import com.oladushek.tictactoe.web.dto.game.OpponentType;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public interface GameService {
    Board play(UUID sessionId, OpponentType opponentType, GameMove gameMove);

    Board clear(UUID sessionId);
}
