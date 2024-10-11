package com.oladushek.tictactoe.web.controller;

import com.oladushek.tictactoe.service.GameService;
import com.oladushek.tictactoe.web.dto.game.GameMove;
import com.oladushek.tictactoe.web.dto.game.Board;
import com.oladushek.tictactoe.web.dto.game.OpponentType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TicTacToeController {

    private final SimpMessagingTemplate template;

    private final GameService gameService;

    @MessageMapping("/play/{sessionId}/clear")
    public void clear(@DestinationVariable String sessionId) {
        Board clearState = gameService.clear(UUID.fromString(sessionId));
        template.convertAndSend("/topic/play/" + sessionId, clearState);
    }

    @MessageMapping("/play/{sessionId}/opponent/{opponentType}")
    public void play(@DestinationVariable String sessionId,
                        @DestinationVariable String opponentType,
                        GameMove move) {

        OpponentType type = opponentType.equals("person") ? OpponentType.PERSON : OpponentType.COMPUTER;

        Board newState = gameService.play(UUID.fromString(sessionId), type, move);
        String destination = "/topic/play/" + sessionId + "/opponent/" + opponentType;
        template.convertAndSend(destination, newState);
    }


}
