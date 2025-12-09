package br.com.quale.controller;

import br.com.quale.dto.*;
import br.com.quale.service.ChatService;
import br.com.quale.service.FileValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Tag(name="Chats", description="API para gerenciamento de chats")
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRestController {

    // Dependências
    private final ChatService chatService;
    private final FileValidationService fileValidationService;


    // Endpoints

    @GetMapping("/")
    public ResponseEntity<List<ChatResponseDTO>> getAllChats() {
        List<ChatResponseDTO> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @PostMapping(value = "/new-group", consumes = "multipart/form-data")
    @Operation(summary = "Criar um novo chat em grupo", description = "Cria um novo chat em grupo com os dados fornecidos.")
    public ResponseEntity<GroupChatResponseDTO> createGroupChat(@Parameter(
                                                                    description = "Não é possível testar este endpoint pelo Swagger UI. Utilize uma ferramenta como Postman ou Insomnia para enviar requisições com o campo data como 'application/json'."
                                                            )
                                                            @RequestPart(name = "data", required = true) @Valid CreateGroupChatDTO groupChatData,
                                                                @RequestPart(name = "file", required = false) MultipartFile file) {
        fileValidationService.validateImageFile(file);
        GroupChatResponseDTO createdChat = chatService.createGroupChat(groupChatData, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChat);
    }

}
