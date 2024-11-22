package sk.management.implementation.jdbc.repository.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.management.api.ProjectService;
import sk.management.api.TaskService;
import sk.management.api.UserService;
import sk.management.api.exception.BadRequestException;
import sk.management.api.request.TaskAddRequest;
import sk.management.api.request.TaskEditRequest;
import sk.management.domain.Project;
import sk.management.domain.Task;
import sk.management.domain.TaskStatus;
import sk.management.implementation.jdbc.repository.TaskJdbcRepository;

@Service
public class TaskServiceJdbcImpl implements TaskService {

	public final TaskJdbcRepository repository;
	public final UserService userService;
	public final ProjectService projectService;

	public TaskServiceJdbcImpl(TaskJdbcRepository repository, UserService userService, ProjectService projectService) {
		this.repository = repository;
		this.userService = userService;
		this.projectService = projectService;
	}

	@Override
	public long add(TaskAddRequest request) {
		// TODO Auto-generated method stub
		return repository.add(request);
	}

	@Override
	public void edit(long taskId, TaskEditRequest request) {
		// TODO Auto-generated method stub
		if (this.get(taskId) != null) {
			repository.update(taskId, request);
		}
	}

	@Override
	public void changeStatus(long taskId, TaskStatus status) {
		// TODO Auto-generated method stub
		if (this.get(taskId) != null) {
			repository.updateStatus(taskId, status);
		}

	}

	@Override
	public void assignProject(long taskId, long projectId) {
		// TODO Auto-generated method stub
		final Task task = this.get(taskId);
		final Project project = projectService.get(projectId);

		if (task != null && project != null) {
			if (task.getUserId() != project.getUserId()) {
				throw new BadRequestException("Task and project must belong to the same user");
			}
			repository.updateProject(taskId, projectId);
		}
	}

	@Override
	public void delete(long taskId) {
		// TODO Auto-generated method stub
		if (this.get(taskId) != null) {
			repository.delete(taskId);
		}
	}

	@Override
	public Task get(long taskId) {
		// TODO Auto-generated method stub
		return repository.getById(taskId);
	}

	@Override
	public List<Task> getAll() {
		// TODO Auto-generated method stub
		return repository.getAll();
	}

	@Override
	public List<Task> getAllByUserId(long userId) {
		// TODO Auto-generated method stub
		if (userService.get(userId) != null) {
			return repository.getAllByUser(userId);
		}

		return null;
	}

	@Override
	public List<Task> getAllByProjectId(long projectId) {
		// TODO Auto-generated method stub
		if (projectService.get(projectId) != null) {
			return repository.getAllByProject(projectId);
		}
		return null;
	}

}
