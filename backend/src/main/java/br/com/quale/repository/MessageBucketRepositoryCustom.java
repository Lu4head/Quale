package br.com.quale.repository;

import br.com.quale.dto.Message;

public interface MessageBucketRepositoryCustom {

    void saveMessage(String chatId, Message message);

}