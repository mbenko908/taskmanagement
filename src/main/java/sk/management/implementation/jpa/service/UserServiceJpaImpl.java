package sk.management.implementation.jpa.service;

import java.util.List;

//import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import sk.management.api.UserService;
import sk.management.api.exception.BadRequestException;
import sk.management.api.exception.InternalErrorException;
import sk.management.api.exception.ResourceNotFoundException;
import sk.management.api.request.UserAddRequest;
import sk.management.domain.User;
import sk.management.implementation.jpa.entity.UserEntity;
import sk.management.implementation.jpa.repository.UserJpaRepository;

@Service()
@Profile("jpa")
public class UserServiceJpaImpl implements UserService {

	private final UserJpaRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceJpaImpl.class);

	public UserServiceJpaImpl(UserJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public long add(UserAddRequest request) {
		// TODO Auto-generated method stub
		try {
			return repository.save(new UserEntity(request.getName(), request.getEmail())).getId();
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException("User with email " + request.getEmail() + "already exists");
		} catch (DataAccessException e) {
			logger.error("Error while adding user", e);
			throw new InternalErrorException("Error while adding user");
		}

	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		if (this.get(id) != null) {
			repository.deleteById(id);
		}
	}

	@Override
	public User get(long id) {
		// TODO Auto-generated method stub
//		final Optional<UserEntity> userEntityOptional = repository.findById(id);
//		if (userEntityOptional.isPresent()) {
//			return mapUserEntity(userEntityOptional.get());
//		} else {
//			throw new ResourceNotFoundException("User with id:" + id + " was not found");
//		}
		return repository.findById(id).map(this::mapUserEntityToUser)
				.orElseThrow(() -> new ResourceNotFoundException("User with id:" + id + "was not found"));
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll().stream().map(this::mapUserEntityToUser).toList();
	}

	private User mapUserEntityToUser(UserEntity entity) {
		return new User(entity.getId(), entity.getName(), entity.getEmail());
	}

}
