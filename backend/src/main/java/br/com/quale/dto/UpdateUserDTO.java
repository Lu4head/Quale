package br.com.quale.dto;

import br.com.quale.enums.UserTypeEnum;
import br.com.quale.validations.Telefone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @Length(min=2, max=120)
    private String name;

    @Email
    private String email;

    @Telefone
    private String phoneNumber;

    private UserTypeEnum userType;

    private Boolean active;

    private Boolean removeProfilePhoto = false;

}