package br.com.quale.mappers;

import br.com.quale.dto.CreateUserDTO;
import br.com.quale.dto.UpdateUserDTO;
import br.com.quale.dto.UserReponseDTO;
import br.com.quale.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginDate", ignore = true)
    @Mapping(target = "profilePhotoUrl", source = "photoUrl")
    @Mapping(target = "password", source = "hashedPassword")
    User toEntity(CreateUserDTO dto, String photoUrl, String hashedPassword);

    UserReponseDTO toResponse(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           // Nunca atualiza ID
    @Mapping(target = "createdAt", ignore = true)  // Nunca atualiza data de criação
    @Mapping(target = "lastLoginDate", ignore = true) // Nunca atualiza data de último login
    @Mapping(target = "profilePhotoUrl", ignore = true)      // Foto tratamos manualmente no service
    @Mapping(target = "password", ignore = true)
    void updateEntityFromDto(UpdateUserDTO dto, @MappingTarget User entity);
}