package br.com.quale.repository;

import br.com.quale.dto.LastMessage;
import br.com.quale.dto.Message;
import br.com.quale.entity.Chat;
import br.com.quale.entity.MessageBucket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@RequiredArgsConstructor
public class MessageBucketRepositoryImpl implements MessageBucketRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private static final int BUCKET_SIZE = 50; // Define o tamanho máximo do bucket

    @Override
    public void saveMessage(String chatId, Message message) {

        // 1. TENTATIVA DE UPDATE (Cenário mais comum e rápido)
        // Busca um bucket desse chat que NÃO esteja cheio (count < 50)
        // Ordena pelo index decrescente para pegar o mais recente
        Query query = new Query(Criteria.where("chat_id").is(chatId)
                .and("messagesCount").lt(BUCKET_SIZE));
        query.with(Sort.by(Sort.Direction.DESC, "bucket_index"));
        query.limit(1);

        Update update = new Update()
                .push("history", message) // Adiciona ao array
                .inc("messagesCount", 1)          // Incrementa contador atomicamente
                .set("endDate", message.getTimestamp()); // Atualiza data final

        MessageBucket updated = mongoTemplate.findAndModify(
                query,
                update,
                org.springframework.data.mongodb.core.FindAndModifyOptions.options().returnNew(true),
                MessageBucket.class
        );

        // 2. SE NÃO HOUVE ATUALIZAÇÃO, CRIA UM NOVO BUCKET
        if (updated == null) {
            createNewBucket(chatId, message);
        }

        // 3. ATUALIZA A "LAST MESSAGE" NA COLEÇÃO CHATS
        updateChatLastMessage(chatId, message);
    }

    private void createNewBucket(String chatId, Message message) {
        // Descobre qual o próximo index
        Query indexQuery = new Query(Criteria.where("chat_id").is(chatId));
        indexQuery.with(Sort.by(Sort.Direction.DESC, "bucket_index"));
        indexQuery.limit(1);

        MessageBucket lastBucket = mongoTemplate.findOne(indexQuery, MessageBucket.class);
        int nextIndex = (lastBucket == null) ? 0 : lastBucket.getBucketIndex() + 1;

        // Cria o novo objeto
        MessageBucket newBucket = new MessageBucket();
        newBucket.setChatId(chatId);
        newBucket.setBucketIndex(nextIndex);
        newBucket.setMessagesCount(1);
        newBucket.setStartDate(message.getTimestamp());
        newBucket.setEndDate(message.getTimestamp());
        newBucket.setHistory(List.of(message));

        mongoTemplate.save(newBucket);
    }

    private void updateChatLastMessage(String chatId, Message message) {
        // Converte sua Message (bucket) para LastMessage (chat preview)
        LastMessage lastMsg = new LastMessage(
                message.getType(),
                message.getContent(),
                message.getSenderId(),
                message.getTimestamp(),
                message.getStatus()
        );

        Query query = new Query(Criteria.where("id").is(chatId));
        Update update = new Update().set("lastMessage", lastMsg);

        // "fire and forget" - atualiza a coleção de Chats
        mongoTemplate.updateFirst(query, update, Chat.class);
    }
}