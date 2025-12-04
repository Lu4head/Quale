package br.com.quale.entity;

import br.com.quale.enums.UserTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "users")
@Entity
@Data
@EqualsAndHashCode(of="id")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;
    
    private Boolean active;
    
    private UserTypeEnum userType;
}
