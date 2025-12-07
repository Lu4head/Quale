package br.com.quale.service;

import br.com.quale.dto.CreateUserDTO;
import br.com.quale.dto.UpdateUserDTO;
import br.com.quale.dto.UserReponseDTO;
import br.com.quale.entity.User;
import br.com.quale.enums.UserTypeEnum;
import br.com.quale.exception.DuplicateResourceException;
import br.com.quale.exception.NoContentException;
import br.com.quale.mappers.UserMapper;
import br.com.quale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    // Dependências
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final UserMapper mapper;


    // Métodos
    public List<UserReponseDTO> getAllUsers(Pageable pageable)  {
        return userRepository.findAll(pageable).getContent().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public UserReponseDTO getUserById(Long id) {
        return userRepository.findByIdWithContacts(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NoContentException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserReponseDTO createUser(CreateUserDTO userData, MultipartFile file) {
        if(userRepository.existsByEmail(userData.getEmail())) {
            throw new DuplicateResourceException("Email já está em uso: " + userData.getEmail());
        }
        if(userRepository.existsByPhoneNumber(userData.getPhoneNumber())){
            throw new DuplicateResourceException("Número de telefone já está em uso: " + userData.getPhoneNumber());
        }
        String userPhotoUrl = null;
        try{
            if (file != null && !file.isEmpty()) {
                userPhotoUrl = fileStorageService.storeFile(file, "profile-pics");
            }
            String hashedPassword = passwordEncoder.encode(userData.getPassword());
            if (userData.getActive() == null){
                userData.setActive(false);
            }
            if (userData.getUserType() == null){
                userData.setUserType(UserTypeEnum.DEFAULT);
            }
            User user = mapper.toEntity(userData, userPhotoUrl, hashedPassword);
            userRepository.save(user);
            return mapper.toResponse(user);
        } catch (Exception e) {
            if(userPhotoUrl != null) {
                fileStorageService.deleteFile(userPhotoUrl, "profile-pics");
            }
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public UserReponseDTO updateUser(Long id, UpdateUserDTO userData, MultipartFile file) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Usuário não encontrado com o ID: " + id));

        if (userData.getEmail() != null && !userData.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(userData.getEmail(), existingUser.getId())) {
                throw new DuplicateResourceException("E-mail já está em uso por outro usuário.");
            }
        }

        if (userData.getPhoneNumber() != null && !userData.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumberAndIdNot(userData.getPhoneNumber(), existingUser.getId())) {
                throw new DuplicateResourceException("Número de telefone já está em uso por outro usuário.");
            }
        }

        mapper.updateEntityFromDto(userData, existingUser);
        if (userData.getRemoveProfilePhoto() == true) {
            String oldPhotoUrl = existingUser.getProfilePhotoUrl();
            existingUser.setProfilePhotoUrl(null);
            if (oldPhotoUrl != null) {
                fileStorageService.deleteFile(oldPhotoUrl, "profile-pics");
            }
        }
        if (file != null && !file.isEmpty()) {
            String oldPhotoUrl = existingUser.getProfilePhotoUrl();
            String newPhotoUrl = null;

            try {
                newPhotoUrl = fileStorageService.storeFile(file, "profile-pics");
                existingUser.setProfilePhotoUrl(newPhotoUrl);

                if (oldPhotoUrl != null) {
                    fileStorageService.deleteFile(oldPhotoUrl, "profile-pics");
                }
            } catch (Exception e) {
                if (newPhotoUrl != null) fileStorageService.deleteFile(newPhotoUrl, "profile-pics");
                throw e;
            }
        }
        User updatedUser = userRepository.save(existingUser);
        return mapper.toResponse(updatedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Usuário não encontrado com o ID: " + id));
        if (user.getProfilePhotoUrl() != null) {
            fileStorageService.deleteFile(user.getProfilePhotoUrl(), "profile-pics");
        }
        userRepository.delete(user);
    }

    public UserReponseDTO activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Usuário não encontrado com o ID: " + id));
        user.setActive(true);
        userRepository.save(user);
        return mapper.toResponse(user);
    }

    public UserReponseDTO setUserAdmin(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUserType(UserTypeEnum.ADMIN);
        userRepository.save(user);
        return mapper.toResponse(user);
    }

    public void addContactToUser(Long user_id, Long contact_id) {
        User user = userRepository.findByIdWithContacts(user_id)
                .orElseThrow(() -> new NoContentException("Usuário não encontrado com o ID: " + user_id));
        User contact = userRepository.findById(contact_id)
                .orElseThrow(() -> new NoContentException("Contato não encontrado com o ID: " + contact_id));
        if (user.getContacts().contains(contact)) {
            throw new DuplicateResourceException("O usuário já está na lista de contatos.");
        }
        user.getContacts().add(contact);
        userRepository.save(user);
    }
}
