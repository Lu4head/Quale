package br.com.quale.entity;

import br.com.quale.dto.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "message_buckets")
@Data
@EqualsAndHashCode(of="id")
@CompoundIndexes({
        @CompoundIndex(name = "chat_bucket_idx", def = "{'chatId': 1, 'bucketIndex': 1}", unique = true)
})
public class MessageBucket {
    @Id
    private String id;

    @Field("chat_id")
    private String chatId;

    @Field("bucket_index")
    private Integer bucketIndex;

    @Field("messagesCount")
    private Integer messagesCount = 0;

    @Field("startDate")
    private Instant startDate;

    @Field("endDate")
    private Instant endDate;

    @Field("history")
    private List<Message> history = new ArrayList<>();

}

