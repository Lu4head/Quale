package br.com.quale.dto;

import br.com.quale.entity.User;
import br.com.quale.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReponseDTO {
    Long id;
    String name;
    String email;
    String phoneNumber;
    Boolean active;
    UserTypeEnum userType;
    String profilePhotoUrl;

    public UserReponseDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.active = user.getActive();
        this.userType = user.getUserType();
        this.profilePhotoUrl = user.getProfilePhotoUrl();
    }
}
