package br.com.quale.controller;

import br.com.quale.dto.ChatResponseDTO;
import br.com.quale.dto.CreateGroupChatDTO;
import br.com.quale.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Chats", description="API para gerenciamento de chats")
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    // Dependências
    private final ChatService chatService;

    @GetMapping("/")
    public ResponseEntity<List<ChatResponseDTO>> getAllChats() {
        List<ChatResponseDTO> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @PostMapping("/new-group")
    public ResponseEntity<ChatResponseDTO> createGroupChat(@RequestBody CreateGroupChatDTO groupChatData) {
        ChatResponseDTO createdChat = chatService.createGroupChat(groupChatData);
        return ResponseEntity.ok(createdChat);
    }

}
