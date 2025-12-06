package br.com.quale.dto;

import br.com.quale.enums.MessageStatusEnum;
import br.com.quale.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String msgId;
    private Long senderId;
    private MessageTypeEnum type;
    private String content;
    private Instant timestamp;
    private MessageStatusEnum status;
}
