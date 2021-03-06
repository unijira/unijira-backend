package it.unical.unijira.data.dao;

import it.unical.unijira.data.models.ProductBacklog;
import it.unical.unijira.data.models.Sprint;
import it.unical.unijira.data.models.projects.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Query(value = "FROM Sprint sprint where sprint.backlog = :backlog")
    List<Sprint> sprintsOfABacklog(ProductBacklog backlog, Pageable pageable);

    @Query(value = "FROM Sprint sprint where sprint.backlog.project = :project and sprint.status = 'ACTIVE'")
    Sprint activeSprint(Project project);
}
