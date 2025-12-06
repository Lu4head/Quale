package br.com.quale.entity;

import br.com.quale.dto.LastMessage;
import br.com.quale.enums.ChatTypeEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "chats")
@Data
@EqualsAndHashCode(of="id")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PrivateChat.class, name = "PRIVATE"),
        @JsonSubTypes.Type(value = GroupChat.class, name = "GROUP")
})
public abstract class Chat {
    @Id
    private String id;

    private ChatTypeEnum type;

    @Indexed
    private List<Long> participantIds;

    private Instant createdAt;

    private LastMessage lastMessage;

}

