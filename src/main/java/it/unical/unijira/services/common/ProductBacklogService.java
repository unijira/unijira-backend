package it.unical.unijira.services.common;

import it.unical.unijira.data.models.ProductBacklog;
import it.unical.unijira.data.models.Item;
import it.unical.unijira.data.models.Project;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductBacklogService {

    /*
        Optional<ProductBacklogItem> save (ProductBacklogItem pbi);
    Optional<ProductBacklogItem> update (Long id, ProductBacklogItem pbi);
    void delete(ProductBacklogItem pbi);
    Optional<ProductBacklogItem> findById(Long id);
    List<ProductBacklogItem> findAll();
    List<ProductBacklogItem> findAllByFather(Long fatherId, int page, int size);
    List<ProductBacklogItem> findAllByUser(Long userId, int page, int size);

     */

    Optional<ProductBacklog> save(ProductBacklog backlog);
    Optional<ProductBacklog> update (Long id, ProductBacklog backlog);
    void delete (ProductBacklog backlog);
    Optional<ProductBacklog> findById(Long id);
    List<ProductBacklog> findAll();
    List<Item> findItems(ProductBacklog backlog);

    List<ProductBacklog> findAllByProject(Project project);
}
