package it.unical.unijira.data.dao.projects;

import it.unical.unijira.data.models.projects.releases.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long>, JpaSpecificationExecutor<Release> {
    List<Release> findAllByProjectIdOrderByCreatedAtDesc(Long projectId);
}