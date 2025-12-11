package br.com.quale.mappers;

import br.com.quale.dto.CreateGroupChatDTO;
import br.com.quale.dto.GroupChatResponseDTO;
import br.com.quale.entity.GroupChat;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GroupChatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastMessage", ignore = true)
    @Mapping(target = "groupImageUrl", ignore = true)
    GroupChat toEntity(CreateGroupChatDTO dto);

    GroupChatResponseDTO toResponse(GroupChat entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           // Nunca atualiza ID
    @Mapping(target = "createdAt", ignore = true)  // Nunca atualiza data
    @Mapping(target = "lastMessage", ignore = true) // Nunca atualiza última mensagem
    @Mapping(target = "type", ignore = true) // Tipo de chat não deve ser alterado após criado
    @Mapping(target = "groupImageUrl", ignore = true) // Imagem tratamos manualmente no service
    @Mapping(target = "participantIds", ignore = true) // Participantes não são atualizados aqui
    void updateEntityFromDto(CreateGroupChatDTO dto, @MappingTarget GroupChat entity);
}
