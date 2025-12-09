package br.com.quale.controller;

import br.com.quale.dto.CreateUserDTO;
import br.com.quale.dto.UpdateUserDTO;
import br.com.quale.dto.UserReponseDTO;
import br.com.quale.service.FileValidationService;
import br.com.quale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "Users", description = "API para gerenciamento de usuários")
@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    // Dependências
    private final UserService userService;
    private final FileValidationService fileValidationService;

    // Métodos
    @GetMapping
    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista paginada de todos os usuários.")
    public ResponseEntity<List<UserReponseDTO>> getAllUsers(Pageable pageable) {
        List<UserReponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário por ID", description = "Retorna os detalhes de um usuário específico pelo seu ID.")
    public ResponseEntity<UserReponseDTO> getUserById(@PathVariable @Min(1) Long id) {
        UserReponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com os dados fornecidos.")
    public ResponseEntity<UserReponseDTO> createUser(@Parameter(
                                                                 description = "Não é possível testar este endpoint pelo Swagger UI. Utilize uma ferramenta como Postman ou Insomnia para enviar requisições com o campo data como 'application/json'."
                                                         )
                                                     @RequestPart(name = "data", required = true) @Valid CreateUserDTO userData,
                                                     @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        fileValidationService.validateImageFile(file);
        UserReponseDTO createdUser = userService.createUser(userData, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário", description = "Atualiza os dados de um usuário existente pelo seu ID.")
    public ResponseEntity<UserReponseDTO> updateUser(@PathVariable Long id,
                                                     @Parameter(
                                                             description = "Não é possível testar este endpoint pelo Swagger UI. Utilize uma ferramenta como Postman ou Insomnia para enviar requisições com o campo data como 'application/json'."
                                                     )
                                                     @RequestPart(name = "data", required = true) @Valid UpdateUserDTO userData,
                                                     @RequestPart(name = "file", required = false) MultipartFile file
                                                     ) {
        fileValidationService.validateImageFile(file);
        UserReponseDTO updatedUser = userService.updateUser(id, userData, file);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um usuário", description = "Deleta um usuário existente pelo seu ID.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{user_id}/contact/{contact_id}")
    @Operation(summary = "Adicionar contato a um usuário", description = "Adiciona um contato existente a um usuário pelo ID do usuário e ID do contato.")
    public ResponseEntity<Void> addContactToUser(
            @PathVariable("user_id") Long userId,
            @PathVariable("contact_id") Long contactId) {
        userService.addContactToUser(userId, contactId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{user_id}/contacts")
    @Operation(summary = "Obter contatos de um usuário", description = "Retorna a lista de contatos de um usuário pelo seu ID.")
    public ResponseEntity<List<UserReponseDTO>> getUserContacts(
            @PathVariable("user_id") Long userId) {
        List<UserReponseDTO> contacts = userService.getUserContacts(userId);
        return ResponseEntity.ok(contacts);
    }

    @DeleteMapping("/{user_id}/contact/{contact_id}")
    @Operation(summary = "Remover contato de um usuário", description = "Remove um contato existente de um usuário pelo ID do usuário e ID do contato.")
    public ResponseEntity<Void> removeContactFromUser(
            @PathVariable("user_id") Long userId,
            @PathVariable("contact_id") Long contactId) {
        userService.removeContactFromUser(userId, contactId);
        return ResponseEntity.noContent().build();
    }
}
