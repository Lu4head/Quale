package br.com.quale.repository;

import br.com.quale.entity.MessageBucket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageBucketRepository extends MongoRepository<MessageBucket, String>, MessageBucketRepositoryCustom {
    // Você ainda pode ter métodos padrão aqui se precisar
    // ex: List<MessageBucket> findByChatId(String chatId);
}