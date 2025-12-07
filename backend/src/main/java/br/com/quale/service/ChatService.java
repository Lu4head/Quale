package br.com.quale.service;

import br.com.quale.dto.ChatResponseDTO;
import br.com.quale.dto.CreateGroupChatDTO;
import br.com.quale.entity.GroupChat;
import br.com.quale.enums.ChatTypeEnum;
import br.com.quale.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    // Dependências
    private final ChatRepository chatRepository;

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
}
