package com.oladushek.tictactoe.web.controller;

import com.oladushek.tictactoe.service.GameService;
import com.oladushek.tictactoe.web.dto.GameMove;
import com.oladushek.tictactoe.web.dto.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicTacToeController {
    private final GameService game = new GameService(); // Создаем игру

    @MessageMapping("/play")
    @SendTo("/topic/games")
    public GameState play(GameMove move) {
        GameState newState = game.play(move);
        return newState; // Отправляем новое состояние игры всем подписчикам
    }

}
