package br.com.quale.service;

import br.com.quale.dto.ChatResponseDTO;
import br.com.quale.dto.CreateGroupChatDTO;
import br.com.quale.dto.Message;
import br.com.quale.entity.GroupChat;
import br.com.quale.enums.ChatTypeEnum;
import br.com.quale.exception.UnauthorizedException;
import br.com.quale.repository.ChatRepository;
import br.com.quale.repository.MessageBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    // Métodos
    public List<ChatResponseDTO> getAllChats(){
        return chatRepository.findAll()
                .stream()
                .map(ChatResponseDTO::new)
                .toList();
    }

    public ChatResponseDTO createGroupChat(CreateGroupChatDTO groupChatData){
        GroupChat groupChat = new GroupChat();
        groupChat.setCreatedAt(Instant.now());
        groupChat.setType(ChatTypeEnum.GROUP);
        groupChat.setGroupName(groupChatData.getGroupName());
        groupChat.setGroupDescription(groupChatData.getGroupDescription());
        groupChat.setParticipantIds(groupChatData.getParticipantId());
        chatRepository.save(groupChat);
        return new ChatResponseDTO(groupChat);
    }

    public void processMessage(String chatId, Message message, Principal loggedUser){
        if (!chatRepository.existsById(chatId))
            throw new IllegalArgumentException("Não existe um chat com o ID: " + chatId);

        if (!chatRepository.existsByIdAndParticipantIdsContaining(chatId, message.getSenderId())){
            throw new UnauthorizedException("Usuário não tem permissão para enviar mensagens ao chat com ID: " + chatId);
        }

        // Salva a mensagem no bucket correspondente
        System.out.println("Enviando mensagem para chat com ID: " + chatId);
        System.out.println("Mensagem: " + message.getContent());
        System.out.println("Remetente ID: " + message.getSenderId());
        messageBucketRepository.saveMessage(chatId, message);
        System.out.println("Passou do método save, ent deveria ter salvo a msg no banco");
        // Define o destino mensagem para os assinantes do chat via WebSocket
        String destination = "/topic/chat/" + chatId;

        // Envia a mensagem
        messagingTemplate.convertAndSend(destination, message);
    }

}
