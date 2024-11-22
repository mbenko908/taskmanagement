package sk.management.implementation.jdbc.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sk.management.api.exception.InternalErrorException;
import sk.management.api.request.ProjectAddRequest;
import sk.management.api.request.ProjectEditRequest;
import sk.management.domain.Project;
import sk.management.implementation.jdbc.mapper.ProjectRowMapper;

@Repository
public class ProjectJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final ProjectRowMapper rowMapper;

	private static final Logger logger;

	private static final String GET_ALL;
	private static final String GET_BY_ID;
	private static final String GET_ALL_BY_USER;
	private static final String INSERT;
	private static final String DELETE;
	private static final String UPDATE;

	static {

		logger = LoggerFactory.getLogger(ProjectJdbcRepository.class);
		GET_ALL = "SELECT * FROM project";
		GET_BY_ID = "SELECT * FROM project WHERE id = ?";
		GET_ALL_BY_USER = "SELECT * FROM project WHERE user_id = ?";
		INSERT = "INSERT INTO project(user_id, name, description, created_at) VALUES (?, ?, ?, ?)";// nextval('public.user_id_seq'),
		DELETE = "DELETE FROM project WHERE id = ?";
		UPDATE = "UPDATE project SET name = ?, description = ? WHERE id = ?";
	}

	public ProjectJdbcRepository(JdbcTemplate jdbcTemplate, ProjectRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Project> getAll() {
		try {
			return jdbcTemplate.query(GET_ALL, rowMapper);
		} catch (DataAccessException e) {
			logger.error("Error while getting all projects", e);
			throw new InternalErrorException("Error while getting all projects");
		}
	}

	public Project getById(long id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			logger.error("Problem with getById project", e);
			throw new InternalErrorException("Project with getById project");
		}
	}

	public List<Project> getAllByUserId(long userId) {
		try {
			return jdbcTemplate.query(GET_ALL_BY_USER, rowMapper, userId);
		} catch (DataAccessException e) {
			logger.error("Error while getting all projects by user id", e);
			throw new InternalErrorException("Error while getting all projects by user id");
		}

	}

	public long add(ProjectAddRequest request) {
		try {
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				final PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, request.getUserId());
				ps.setString(2, request.getName());
				if (request.getDescription() != null) {
					ps.setString(3, request.getDescription());
				} else {
					ps.setNull(3, java.sql.Types.VARCHAR);
				}
				ps.setTimestamp(4, Timestamp.from(OffsetDateTime.now().toInstant()));
				return ps;
			}, keyHolder);

			if (keyHolder.getKeyList() == null) {
				logger.error("Error while adding project, keyHolder.getKey() is null");
				throw new InternalErrorException("Error while adding project");
			}

			// return keyHolder.getKey().longValue();
			Map<String, Object> keys = keyHolder.getKeyList().get(0);
			Long id = (Long) keys.get("id");
			return id;
		} catch (DataAccessException e) {
			logger.error("Error while adding project", e);
			throw new InternalErrorException("Error while adding project");
		}
	}

	public void update(ProjectEditRequest request) {
		try {
			jdbcTemplate.update(UPDATE, request);
		} catch (DataAccessException e) {
			logger.error("Error while updating project", e);
			throw new InternalErrorException("Error while updating project");
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
