package br.com.quale.controller;

import br.com.quale.dto.MessageDTO;
import br.com.quale.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;

    @MessageMapping("/chat/{chatId}/send")
    public void receiveMessage(@DestinationVariable String chatId, @Valid MessageDTO message, Principal principal) {
        chatService.processMessage(chatId, message, principal);
    }
}
