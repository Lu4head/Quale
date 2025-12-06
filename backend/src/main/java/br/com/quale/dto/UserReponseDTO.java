package br.com.quale.dto;

import br.com.quale.entity.User;
import br.com.quale.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReponseDTO {
    Long id;
    String name;
    String email;
    Boolean active;
    UserTypeEnum userType;

    public UserReponseDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.active = user.getActive();
        this.userType = user.getUserType();
    }
}
