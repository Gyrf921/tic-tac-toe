package com.oladushek.tictactoe.web.controller;

import com.oladushek.tictactoe.service.GamePvEService;
import com.oladushek.tictactoe.service.GamePvPService;
import com.oladushek.tictactoe.service.model.Mark;
import com.oladushek.tictactoe.web.dto.ChatMessage;
import com.oladushek.tictactoe.web.dto.GameMove;
import com.oladushek.tictactoe.web.dto.Board;
import com.oladushek.tictactoe.web.dto.Greeting;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class TicTacToeController {
    private Board board = new Board();
    private final GamePvPService gamePvP = new GamePvPService(); // Создаем игру
    private final GamePvEService gamePvE = new GamePvEService(); // Создаем игру

    @MessageMapping("/play/clear")
    @SendTo("/topic/games/clear")
    public Board clearPvE() {
        return board = new Board(); // Отправляем новое состояние игры всем подписчикам
    }

    @MessageMapping("/play/PvP")
    @SendTo("/topic/games/PvP")
    public Board playPvP(GameMove move) {
        Board newState = gamePvP.play(move, board);
        return newState; // Отправляем новое состояние игры всем подписчикам
    }

    @MessageMapping("/play/PvE")
    @SendTo("/topic/games/PvE")
    public Board playPvE(GameMove move) {
        board.setCurrentPlayer(Mark.valueOf(move.getPlayer()));
        Board newState = gamePvE.play(move, board);
        return newState; // Отправляем новое состояние игры всем подписчикам
    }


    @MessageMapping("/message")
    @SendTo("/topic/greetings")
    public Greeting greeting(ChatMessage message) throws Exception {
        return new Greeting( HtmlUtils.htmlEscape(message.getName()) + ": " + HtmlUtils.htmlEscape(message.getMessage()) );
    }


}
