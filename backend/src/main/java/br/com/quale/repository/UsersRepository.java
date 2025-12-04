package br.com.quale.repository;

import br.com.quale.dto.UserReponseDTO;
import br.com.quale.entity.Users;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<@NonNull Users, @NonNull Long> {
    Optional<Users> findByEmail(String email);

    Page<@NonNull Users> findAll(Pageable pageable);
}
