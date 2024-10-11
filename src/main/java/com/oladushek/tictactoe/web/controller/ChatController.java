package com.oladushek.tictactoe.web.controller;

import com.oladushek.tictactoe.web.dto.chat.ChatMessage;
import com.oladushek.tictactoe.web.dto.chat.HtmlChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/play/{sessionId}/chat")
    public void greeting(@DestinationVariable String sessionId,
                             ChatMessage message) throws Exception {
        template.convertAndSend("/topic/play/" + sessionId + "/chat",
                new HtmlChatMessageDto(HtmlUtils.htmlEscape(message.getName()) + ": " + HtmlUtils.htmlEscape(message.getMessage())));
    }


}
