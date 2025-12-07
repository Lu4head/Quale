package br.com.quale.entity;

import br.com.quale.enums.UserTypeEnum;
import br.com.quale.validations.Telefone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@EqualsAndHashCode(of="id")
@EntityListeners(AuditingEntityListener.class)
public class User {

    // --- ATRIBUTOS ---

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 120, nullable = false)
    private String name;

    @Email(message = "Formato de e-mail inválido")
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserTypeEnum userType;

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    @Telefone(message = "Número de telefone inválido")
    private String phoneNumber;

    @Column(name = "profile_photo_url", length = 255)
    private String profilePhotoUrl;

    // --- LISTA DE CONTATOS ---
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_contacts", // Nome da tabela técnica no banco
            joinColumns = @JoinColumn(name = "user_id"), // Quem é o dono da agenda
            inverseJoinColumns = @JoinColumn(name = "contact_id") // Quem é o contato
    )
    private Set<User> contacts = new HashSet<>();

    // --- CAMPOS DE AUDITORIA ---
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;


    // --- MÉTODOS ---

    public void addContact(User contact) {
        this.contacts.add(contact);
    }
}
