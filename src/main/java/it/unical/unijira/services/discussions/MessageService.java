package it.unical.unijira.services.discussions;


import it.unical.unijira.data.models.discussions.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    Optional<Message> save (Message message);
    Optional<Message> update (Long id, Message message, Long projectId, Long topicId);
    void delete(Message message, Long projectId, Long topicId);
    Optional<Message> findById(Long id, Long topicId);
    List<Message> findAll(Long topicId, int page, int size);

    Integer countByTopic(Long topicId);
}
