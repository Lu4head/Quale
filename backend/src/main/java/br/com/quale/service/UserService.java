package br.com.quale.service;

import br.com.quale.dto.CreateUserDTO;
import br.com.quale.dto.UserReponseDTO;
import br.com.quale.entity.User;
import br.com.quale.enums.UserTypeEnum;
import br.com.quale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    // Dependências
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Métodos
    public Page<UserReponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserReponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getActive(),
                        user.getUserType()
                ));
    }

    public UserReponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserReponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getActive(),
                        user.getUserType()
                ))
                .orElse(null);
    }

    @Transactional
    public UserReponseDTO createUser(CreateUserDTO userData) {
        User user = new User();
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        user.setActive(false);
        userRepository.save(user);
        return new UserReponseDTO(user);
    }

    @Transactional
    public UserReponseDTO updateUser(Long id, CreateUserDTO userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        userRepository.save(user);
        return new UserReponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public UserReponseDTO activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(true);
        userRepository.save(user);
        return new UserReponseDTO(user);
    }

    public UserReponseDTO setUserAdmin(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUserType(UserTypeEnum.ADMIN);
        userRepository.save(user);
        return new UserReponseDTO(user);
    }
}
