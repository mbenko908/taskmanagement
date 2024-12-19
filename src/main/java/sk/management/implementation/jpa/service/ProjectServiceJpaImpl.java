package sk.management.implementation.jpa.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import sk.management.api.ProjectService;
import sk.management.api.UserService;
import sk.management.api.exception.InternalErrorException;
import sk.management.api.exception.ResourceNotFoundException;
import sk.management.api.request.ProjectAddRequest;
import sk.management.api.request.ProjectEditRequest;
import sk.management.domain.Project;
import sk.management.domain.User;
import sk.management.implementation.jpa.entity.ProjectEntity;
import sk.management.implementation.jpa.entity.UserEntity;
import sk.management.implementation.jpa.repository.ProjectJpaRepository;

@Service
@Profile("jpa")
public class ProjectServiceJpaImpl implements ProjectService {

	private final ProjectJpaRepository repository;

	private final UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceJpaImpl.class);

	private ProjectServiceJpaImpl(ProjectJpaRepository repository, UserService userService) {
		this.repository = repository;
		this.userService = userService;
	}

	@Override
	public Project get(long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).map(this::mapProjectEntityToProject)
				.orElseThrow(() -> new ResourceNotFoundException("Project with id: " + id + " was not found"));
	}

	@Override
	public List<Project> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll().stream().map(this::mapProjectEntityToProject).toList();
	}

	@Override
	public List<Project> getAllByUser(long userId) {
		// TODO Auto-generated method stub
		if (userService.get(userId) != null) {
			return repository.findAllByUserId(userId).stream().map(this::mapProjectEntityToProject).toList();
		}
		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		if (this.get(id) != null) {
			repository.deleteById(id);
		}

	}

	@Override
	public long add(ProjectAddRequest request) {
		// TODO Auto-generated method stub
		final User user = userService.get(request.getUserId());
		final UserEntity userEntity = new UserEntity(user.getId(), user.getName(), user.getEmail());
		try {
			return repository.save(
					new ProjectEntity(userEntity, request.getName(), request.getDescription(), OffsetDateTime.now()))
					.getId();
		} catch (DataAccessException e) {
			logger.error("Error while adding project", e);
			throw new InternalErrorException("Error while adding project");
		}
	}

	@Override
	public void edit(long id, ProjectEditRequest request) {
		// TODO Auto-generated method stub
		final ProjectEntity projectEntity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Projectwith id: " + id + "was not found"));

		projectEntity.setName(request.getName());
		projectEntity.setDescription(request.getDescription());

		repository.save(projectEntity);

	}

	private Project mapProjectEntityToProject(ProjectEntity entity) {
		return new Project(entity.getId(), entity.getUser().getId(), entity.getName(), entity.getDescription(),
				entity.getCreatedAt());
	}
}
