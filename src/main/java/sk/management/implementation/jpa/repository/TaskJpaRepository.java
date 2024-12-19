package sk.management.implementation.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sk.management.implementation.jpa.entity.TaskEntity;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

	List<TaskEntity> findAllByUserId(Long userId);

	List<TaskEntity> findAllByProjectId(Long projectId);
}
