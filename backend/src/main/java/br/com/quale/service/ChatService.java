package br.com.quale.service;

import br.com.quale.dto.*;
import br.com.quale.entity.GroupChat;
import br.com.quale.enums.ChatTypeEnum;
import br.com.quale.exception.UnauthorizedException;
import br.com.quale.mappers.GroupChatMapper;
import br.com.quale.mappers.PrivateChatMapper;
import br.com.quale.repository.ChatRepository;
import br.com.quale.repository.MessageBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    // Dependências
    private final ChatRepository chatRepository;
    private final MessageBucketRepository messageBucketRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final PrivateChatMapper privateChatMapper;
    private final GroupChatMapper groupChatMapper;
    private final FileStorageService fileStorageService;

    // Métodos
    public List<ChatResponseDTO> getAllChats(){
        return chatRepository.findAll()
                .stream()
                .map(ChatResponseDTO::new)
                .toList();
    }

    public GroupChatResponseDTO createGroupChat(CreateGroupChatDTO groupChatData, MultipartFile file){
        String groupImageUrl = null;

        // Armazena a imagem do grupo, se fornecida
        if (file != null && !file.isEmpty()) {
            groupImageUrl = fileStorageService.storeFile(file, "group-chats-images");
        }

        GroupChat groupChat = groupChatMapper.toEntity(groupChatData);
        groupChat.setCreatedAt(Instant.now());
        groupChat.setType(ChatTypeEnum.GROUP);
        groupChat.setGroupImageUrl(groupImageUrl);

        GroupChat savedGroupChat = chatRepository.save(groupChat);

        return groupChatMapper.toResponse(savedGroupChat);
    }

    public void processMessage(String chatId, MessageDTO message, Principal loggedUser){
        if (!chatRepository.existsById(chatId))
            throw new IllegalArgumentException("Não existe um chat com o ID: " + chatId);

        if(loggedUser != null){
            Long loggedUserId = userService.getUserEntityByEmail(loggedUser.getName()).getId();
            if (message.getSenderId() != null && !loggedUserId.equals(message.getSenderId())){
                throw new UnauthorizedException("O ID do remetente da mensagem não corresponde ao usuário logado.");
            }
            message.setSenderId(loggedUserId);
        }

        if (!chatRepository.existsByIdAndParticipantIdsContaining(chatId, message.getSenderId())){
            throw new UnauthorizedException("Usuário não tem permissão para enviar mensagens ao chat com ID: " + chatId);
        }

        // Salva a mensagem no bucket correspondente
        messageBucketRepository.saveMessage(chatId, new Message(message));

        // Define o destino mensagem para os assinantes do chat via WebSocket
        String destination = "/topic/chat/" + chatId;

        // Envia a mensagem
        messagingTemplate.convertAndSend(destination, message);
    }

}
