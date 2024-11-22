package sk.management.api;

import java.util.List;

import sk.management.api.request.ProjectAddRequest;
import sk.management.api.request.ProjectEditRequest;
import sk.management.domain.Project;

public interface ProjectService {
	Project get(long id);

	List<Project> getAll();

	List<Project> getAllByUser(long userId);

	void delete(long id);

	long add(ProjectAddRequest request);

	void edit(long id, ProjectEditRequest request);
}
