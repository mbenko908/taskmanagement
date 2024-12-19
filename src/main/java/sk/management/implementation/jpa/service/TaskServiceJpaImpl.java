package sk.management.implementation.jpa.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import sk.management.api.ProjectService;
import sk.management.api.TaskService;
import sk.management.api.UserService;
import sk.management.api.exception.BadRequestException;
import sk.management.api.exception.InternalErrorException;
import sk.management.api.exception.ResourceNotFoundException;
import sk.management.api.request.TaskAddRequest;
import sk.management.api.request.TaskEditRequest;
import sk.management.domain.Project;
import sk.management.domain.Task;
import sk.management.domain.TaskStatus;
import sk.management.domain.User;
import sk.management.implementation.jpa.entity.ProjectEntity;
import sk.management.implementation.jpa.entity.TaskEntity;
import sk.management.implementation.jpa.entity.UserEntity;
import sk.management.implementation.jpa.repository.TaskJpaRepository;

@Service
@Profile("jpa")
public class TaskServiceJpaImpl implements TaskService {

	private final TaskJpaRepository repository;
	private final ProjectService projectService;
	private final UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(TaskServiceJpaImpl.class);

	public TaskServiceJpaImpl(TaskJpaRepository repository, ProjectService projectService, UserService userService) {
		this.repository = repository;
		this.projectService = projectService;
		this.userService = userService;
	}

	@Override
	public long add(TaskAddRequest request) {
		// TODO Auto-generated method stub
		final User user = userService.get(request.getUserId());
		final UserEntity userEntity = new UserEntity(user.getId(), user.getName(), user.getEmail());

		final ProjectEntity projectEntity;
		if (request.getProjectId() != null) {
			final Project project = projectService.get(request.getProjectId());
			projectEntity = new ProjectEntity(project.getId(), userEntity, project.getName(), project.getDescription(),
					project.getCreateAt());
		} else {
			projectEntity = null;
		}
		// return 0;

		try {
			return repository.save(new TaskEntity(userEntity, projectEntity, request.getName(),
					request.getDescription(), TaskStatus.NEW, OffsetDateTime.now())).getId();
		} catch (DataAccessException e) {
			logger.error("Error while adding task", e);
			throw new InternalErrorException("Error while adding task");
		}
	}

	@Override
	public void edit(long taskId, TaskEditRequest request) {
		// TODO Auto-generated method stub
		final TaskEntity taskEntity = repository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + "was not found"));

		taskEntity.setName(request.getName());
		taskEntity.setDescription(request.getDescription());
		taskEntity.setStatus(request.getStatus());
		repository.save(taskEntity);

	}

	@Override
	public void changeStatus(long taskId, TaskStatus status) {
		// TODO Auto-generated method stub
		final TaskEntity taskEntity = repository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + "was not found"));
		taskEntity.setStatus(status);
		repository.save(taskEntity);

	}

	@Override
	public void assignProject(long taskId, long projectId) {
		// TODO Auto-generated method stub
		final TaskEntity taskEntity = repository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + "was not found"));

		final Project project = projectService.get(projectId);

		if (!taskEntity.getUser().getId().equals(project.getUserId())) {
			throw new BadRequestException("Task and project must belong to the same user");
		}

		final ProjectEntity projectEntity = new ProjectEntity(project.getId(), taskEntity.getUser(), project.getName(),
				project.getDescription(), project.getCreateAt());

		taskEntity.setProject(projectEntity);// .setProject(projectEntity);
		repository.save(taskEntity);
	}

	@Override
	public void delete(long taskId) {
		// TODO Auto-generated method stub
		if (this.get(taskId) != null) {
			repository.deleteById(taskId);
		}

	}

	@Override
	public Task get(long taskId) {
		// TODO Auto-generated method stub
		return repository.findById(taskId).map(this::mapTaskEntityToTask)
				.orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " was not found"));
	}

	@Override
	public List<Task> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll().stream().map(this::mapTaskEntityToTask).toList();
	}

	@Override
	public List<Task> getAllByUserId(long userId) {
		// TODO Auto-generated method stub
		if (userService.get(userId) != null) {
			return repository.findAllByUserId(userId).stream().map(this::mapTaskEntityToTask).toList();
		}
		return null;
	}

	@Override
	public List<Task> getAllByProjectId(long projectId) {
		// TODO Auto-generated method stub
		if (projectService.get(projectId) != null) {
			return repository.findAllByUserId(projectId).stream().map(this::mapTaskEntityToTask).toList();
		}
		return null;
	}

	private Task mapTaskEntityToTask(TaskEntity taskEntity) {
		return new Task(taskEntity.getId(), taskEntity.getUser().getId(),
				taskEntity.getProject() != null ? taskEntity.getProject().getId() : null, taskEntity.getName(),
				taskEntity.getDescription(), taskEntity.getStatus(), taskEntity.getCreatedAt());
	}

}
