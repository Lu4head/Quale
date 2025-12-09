package br.com.quale.mappers;

import br.com.quale.dto.ChatResponseDTO;
import br.com.quale.dto.CreateChatDTO;
import br.com.quale.entity.Chat;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastMessage", ignore = true)
    Chat toEntity(CreateChatDTO dto);

    ChatResponseDTO toResponse(Chat entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           // Nunca atualiza ID
    @Mapping(target = "createdAt", ignore = true)  // Nunca atualiza data
    @Mapping(target = "lastMessage", ignore = true) // Nunca atualiza última mensagem
    @Mapping(target = "type", ignore = true) // Tipo de chat não deve ser alterado após criado
    void updateEntityFromDto(CreateChatDTO dto, @MappingTarget Chat entity);
}
