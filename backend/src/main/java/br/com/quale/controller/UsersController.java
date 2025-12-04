package br.com.quale.controller;

import br.com.quale.dto.CreateUserDTO;
import br.com.quale.dto.UserReponseDTO;
import br.com.quale.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Users", description = "API para gerenciamento de usuários")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    // Dependências
    private final UsersService usersService;

    // Métodos
    @GetMapping
    public ResponseEntity<Page<UserReponseDTO>> getAllUsers(Pageable pageable) {
        Page<UserReponseDTO> users = usersService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReponseDTO> getUserById(@PathVariable @Min(1) Long id) {
        UserReponseDTO user = usersService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserReponseDTO> createUser(@RequestBody @Valid CreateUserDTO userDTO) {
        UserReponseDTO createdUser = usersService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReponseDTO> updateUser(@PathVariable Long id,
                                                     @RequestBody @Valid CreateUserDTO userDTO) {
        UserReponseDTO updatedUser = usersService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
