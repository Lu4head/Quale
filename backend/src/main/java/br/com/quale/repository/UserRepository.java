package br.com.quale.repository;

import br.com.quale.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.contacts WHERE u.id = :id")
    Optional<User> findByIdWithContacts(Long id);

    @Query("SELECT c FROM User u JOIN u.contacts c WHERE u.id = :userId ORDER BY c.name")
    List<User> findContactsByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

}
