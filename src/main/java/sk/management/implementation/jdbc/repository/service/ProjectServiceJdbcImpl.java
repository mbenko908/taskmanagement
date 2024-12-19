package sk.management.implementation.jdbc.repository.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import sk.management.api.ProjectService;
import sk.management.api.request.ProjectAddRequest;
import sk.management.api.request.ProjectEditRequest;
import sk.management.domain.Project;
import sk.management.implementation.jdbc.repository.ProjectJdbcRepository;
import sk.management.implementation.jdbc.repository.TaskJdbcRepository;
import sk.management.implementation.jdbc.repository.UserJdbcRepository;

@Service
@Profile("jdbc")
public class ProjectServiceJdbcImpl implements ProjectService {

	private final ProjectJdbcRepository projectJdbcRepository;
	private final UserJdbcRepository userJdbcRepository;
	private final TaskJdbcRepository taskJdbcRepository;

	public ProjectServiceJdbcImpl(ProjectJdbcRepository projectJdbcRepository, UserJdbcRepository userJdbcRepository,
			TaskJdbcRepository taskJdbcRepository) {
		this.projectJdbcRepository = projectJdbcRepository;
		this.userJdbcRepository = userJdbcRepository;
		this.taskJdbcRepository = taskJdbcRepository;
	}

	@Override
	public Project get(long id) {
		// TODO Auto-generated method stub
		return projectJdbcRepository.getById(id);
		// return null;
	}

	@Override
	public List<Project> getAll() {
		// TODO Auto-generated method stub
		return projectJdbcRepository.getAll();
	}

	@Override
	public List<Project> getAllByUser(long userId) {
		// TODO Auto-generated method stub
		if (userJdbcRepository.getById(userId) != null) {
			return projectJdbcRepository.getAllByUserId(userId);
		}

		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		if (this.get(id) != null) {
			projectJdbcRepository.delete(id);
			taskJdbcRepository.deleteAllByProject(id);
		}

	}

	@Override
	public long add(ProjectAddRequest request) {
		// TODO Auto-generated method stub
		return projectJdbcRepository.add(request);
	}

	@Override
	public void edit(long id, ProjectEditRequest request) {
		// TODO Auto-generated method stub
		if (this.get(id) != null) {
			projectJdbcRepository.update(request);
		}

	}

}
