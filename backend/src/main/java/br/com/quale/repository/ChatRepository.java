package br.com.quale.repository;

import br.com.quale.entity.Chat;
import br.com.quale.entity.GroupChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantIdsContaining(Long participantId, Pageable pageable);

    @Query("{ 'type': 'GROUP', 'participantIds': ?0 }")
    List<GroupChat> findGroupsByUserId(Long userId);
}
