package sk.management.implementation.jdbc.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sk.management.api.exception.BadRequestException;
import sk.management.api.exception.InternalErrorException;
import sk.management.api.exception.ResourceNotFoundException;
import sk.management.api.request.UserAddRequest;
import sk.management.domain.User;
import sk.management.implementation.jdbc.mapper.UserRowMapper;

@Repository
public class UserJdbcRepository {
	private final JdbcTemplate jdbcTemplate;
	private final UserRowMapper userRowMapper;
	private static final Logger logger;

	private static final String GET_ALL;
	private static final String GET_BY_ID;
	private static final String INSERT;
	private static final String DELETE;

	static { // static block
		logger = LoggerFactory.getLogger(UserJdbcRepository.class);
		GET_ALL = "SELECT * FROM users";
		GET_BY_ID = "SELECT * FROM users WHERE id = ?";
		INSERT = "INSERT INTO users(name, email) VALUES (?, ?)";// nextval('public.user_id_seq'),
		DELETE = "DELETE FROM users WHERE id = ?";
	}

	public UserJdbcRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.userRowMapper = userRowMapper;
	}

	public List<User> getAll() {
		return jdbcTemplate.query(GET_ALL, userRowMapper);
	}

	/**
	 * public long add(UserAddRequest request) { try { final KeyHolder keyHolder =
	 * new GeneratedKeyHolder(); jdbcTemplate.update(connection -> { final
	 * PreparedStatement ps = connection.prepareStatement(INSERT,
	 * Statement.RETURN_GENERATED_KEYS); ps.setString(1, request.getName());
	 * ps.setString(2, request.getEmail()); return ps; }, keyHolder);
	 * 
	 * if (keyHolder.getKey() == null) { logger.error("Error while adding user,
	 * keyHolder.getKey() is null"); throw new InternalErrorException("Error while
	 * adding user"); }
	 * 
	 * return keyHolder.getKey().longValue(); } catch
	 * (DataIntegrityViolationException e) { throw new BadRequestException("User
	 * with email " + request.getEmail() + " already exists"); } catch
	 * (DataAccessException e) { logger.error("Error while adding user", e); throw
	 * new InternalErrorException("Error while adding user"); } }
	 **/

	public long add(UserAddRequest request) {
		try {
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				final PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, request.getName());
				ps.setString(2, request.getEmail());
				return ps;
			}, keyHolder);

			List<Map<String, Object>> keys = keyHolder.getKeyList();
			if (keys.isEmpty()) {
				logger.error("Error while adding user, no generated key found");
				throw new InternalErrorException("Error while adding user");
			}

			Map<String, Object> keyMap = keys.get(0);
			Long userId = (Long) keyMap.get("id");

			if (userId == null) {
				logger.error("Error while adding user, id is null");
				throw new InternalErrorException("Error while adding user");
			}

			return userId;

		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException("User with email " + request.getEmail() + " already exists");
		} catch (DataAccessException e) {
			logger.error("Error while adding user", e);
			throw new InternalErrorException("Error while adding user");
		}
	}

	public User getById(long id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID, userRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("User with id" + id + "was not found");
		} catch (DataAccessException e) {
			logger.error("Error while getting user", e);
			throw new InternalErrorException("Error while getting user by id");
		}
	}

	public void delete(long id) {
		try {
			jdbcTemplate.update(DELETE, id);
		} catch (DataAccessException e) {
			logger.error("Error while deleting user", e);
			throw new InternalErrorException("Error while deleting user");
		}
	}

}
