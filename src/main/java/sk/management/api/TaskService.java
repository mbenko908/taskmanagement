package sk.management.api;

import java.util.List;

import sk.management.api.request.TaskAddRequest;
import sk.management.api.request.TaskEditRequest;
import sk.management.domain.Task;
import sk.management.domain.TaskStatus;

public interface TaskService {

	long add(TaskAddRequest request);

	void edit(long id, TaskEditRequest request);

	void changeStatus(long id, TaskStatus status);

	void assignProject(long taskId, long projectId);

	void delete(long id);

	Task get(long id);

	List<Task> getAll();

	List<Task> getAllByUserId(long userId);

	List<Task> getAllByProjectId(long projectId);
}
