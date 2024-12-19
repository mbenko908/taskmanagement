package sk.management.implementation.jdbc.repository.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import sk.management.api.UserService;
import sk.management.api.request.UserAddRequest;
import sk.management.domain.User;
import sk.management.implementation.jdbc.repository.ProjectJdbcRepository;
import sk.management.implementation.jdbc.repository.TaskJdbcRepository;
import sk.management.implementation.jdbc.repository.UserJdbcRepository;

@Service()
@Profile("jdbc")
public class UserServiceJdbcImpl implements UserService {

	private final UserJdbcRepository userJdbcRepository;
	private final TaskJdbcRepository taskJdbcRepository;
	private final ProjectJdbcRepository projectJdbcRepository;

	public UserServiceJdbcImpl(UserJdbcRepository userJdbcRepository, TaskJdbcRepository taskJdbcRepository,
			ProjectJdbcRepository projectJdbcRepository) {
		this.userJdbcRepository = userJdbcRepository;
		this.taskJdbcRepository = taskJdbcRepository;
		this.projectJdbcRepository = projectJdbcRepository;
	}

	@Override
	public long add(UserAddRequest request) {
		return userJdbcRepository.add(request);
	}

	@Override
	public User get(long id) {
		// TODO Auto-generated method stub
		return userJdbcRepository.getById(id);
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return userJdbcRepository.getAll();
	}

	@Override
	public void delete(long id) {
		if (this.get(id) != null) {
			taskJdbcRepository.deleteAllByUser(id);
			projectJdbcRepository.getAllByUserId(id);
			userJdbcRepository.delete(id);
		}

	}

}
