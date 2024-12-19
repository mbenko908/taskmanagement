package sk.management.implementation.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.management.implementation.jpa.entity.ProjectEntity;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {

	List<ProjectEntity> findAllByUserId(Long userId);
}
