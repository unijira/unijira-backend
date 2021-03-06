package it.unical.unijira.services.discussions;


import it.unical.unijira.data.models.discussions.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {

    Optional<Topic> save (Topic topic);
    Optional<Topic> update (Long id, Topic topic, Long projectId);
    void delete(Topic topic);
    Optional<Topic> findById(Long id, Long projectId);
    List<Topic> findAll(Long projectId, int page, int size);
}
