package br.com.quale.dto;

import br.com.quale.enums.MessageTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    @NotNull(message = "O ID da mensagem não pode ser nulo.")
    private String msgId;

    @Min(value = 1, message = "O ID do remetente deve ser maior que zero.")
    private Long senderId;

    private MessageTypeEnum type = MessageTypeEnum.TEXT;

    private String content;
}
