package br.com.quale.dto;

import br.com.quale.enums.ChatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatDTO {
    private ChatTypeEnum type;
    private List<Long> participantId;
}
