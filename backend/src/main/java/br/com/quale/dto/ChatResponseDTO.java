package br.com.quale.dto;

import br.com.quale.entity.Chat;
import br.com.quale.enums.ChatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {
    private String id;
    private ChatTypeEnum type;
    private List<Long> participantIds;
    private Instant createdAt;
    private LastMessage lastMessage;

    public ChatResponseDTO(Chat chat){
        this.id = chat.getId();
        this.type = chat.getType();
        this.participantIds = chat.getParticipantIds();
        this.createdAt = chat.getCreatedAt();
        this.lastMessage = chat.getLastMessage();
    }
}
