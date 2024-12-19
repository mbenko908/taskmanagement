package sk.management.implementation.jdbc.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sk.management.api.exception.InternalErrorException;
import sk.management.api.request.TaskAddRequest;
import sk.management.api.request.TaskEditRequest;
import sk.management.domain.Task;
import sk.management.domain.TaskStatus;
import sk.management.implementation.jdbc.mapper.TaskRowMapper;

@Repository
public class TaskJdbcRepository {

	private final JdbcTemplate jdbcTemplate;
	private final TaskRowMapper taskRowMapper;

	public TaskJdbcRepository(JdbcTemplate jdbcTemplate, TaskRowMapper taskRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.taskRowMapper = taskRowMapper;
	}

	private static final Logger logger;

	private static final String GET_ALL;
	private static final String GET_BY_ID;
	private static final String GET_ALL_BY_USER;
	private static final String GET_ALL_BY_PROJECT;
	private static final String INSERT;
	private static final String DELETE;
	private static final String DELETE_ALL_BY_USER;
	private static final String DELETE_ALL_BY_PROJECT;
	private static final String UPDATE;
	private static final String UPDATE_PROJECT;
	private static final String UPDATE_STATUS;

	static {

		logger = LoggerFactory.getLogger(ProjectJdbcRepository.class);
		GET_ALL = "SELECT * FROM task";
		GET_BY_ID = "SELECT * FROM task WHERE id = ?";
		GET_ALL_BY_USER = "SELECT * FROM task WHERE user_id = ?";
		GET_ALL_BY_PROJECT = "SELECT * FROM project WHERE user_id = ?";
		INSERT = "INSERT INTO task(user_id, project_id, name, description, status, created_at) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";// nextval('public.user_id_seq'),
		DELETE = "DELETE FROM task WHERE id = ?";
		UPDATE = "UPDATE task SET name = ?, description = ?, status = ? WHERE id = ?";
		UPDATE_PROJECT = "UPDATE task SET project_id = ?, description = ? WHERE id = ?";
		UPDATE_STATUS = "UPDATE task SET status = ?, description = ? WHERE id = ?";
		DELETE_ALL_BY_USER = "DELETE FROM task WHERE user_id = ?";
		DELETE_ALL_BY_PROJECT = "DELETE FROM task WHERE project_id = ?";
	}

	public long add(TaskAddRequest request) {
		try {
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				final PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, request.getUserId());
				if (request.getProjectId() != null) {
					ps.setLong(2, request.getProjectId());
				} else {
					ps.setNull(2, java.sql.Types.BIGINT);
				}
				ps.setString(3, request.getName());
				if (request.getDescription() != null) {
					ps.setString(4, request.getDescription());
				} else {
					ps.setNull(4, java.sql.Types.VARCHAR);
				}
				ps.setString(5, TaskStatus.NEW.toString());
				ps.setTimestamp(6, Timestamp.from(OffsetDateTime.now().toInstant()));
				return ps;
			}, keyHolder);

			if (keyHolder.getKey() == null) {
				logger.error("Error while adding task, keyHolder.getKey() is null");
				throw new InternalErrorException("Error while adding task");
			}

			return keyHolder.getKey().longValue();
		} catch (DataAccessException e) {
			logger.error("Error while adding task", e);
			throw new InternalErrorException("Error while adding task");
		}
	}

	/**
	 * public long add(TaskAddRequest request) { try { final KeyHolder keyHolder =
	 * new GeneratedKeyHolder(); jdbcTemplate.update(connection -> { final
	 * PreparedStatement ps = connection.prepareStatement(INSERT,
	 * Statement.RETURN_GENERATED_KEYS); ps.setLong(1, request.getUserId()); if
	 * (request.getProjectId() != null) { ps.setLong(2, request.getProjectId()); }
	 * else { ps.setNull(2, java.sql.Types.BIGINT); } ps.setString(3,
	 * request.getName()); if (request.getDescription() != null) { ps.setString(4,
	 * request.getDescription()); } else { ps.setNull(4, java.sql.Types.VARCHAR); }
	 * ps.setString(5, TaskStatus.NEW.toString()); ps.setTimestamp(6,
	 * Timestamp.from(OffsetDateTime.now().toInstant())); return ps; }, keyHolder);
	 * 
	 * List<Map<String, Object>> keys = keyHolder.getKeyList();
	 * logger.debug("Generated keys: " + keyHolder.getKeyList());
	 * 
	 * if (keys == null || keys.isEmpty()) { logger.error("Error while adding task:
	 * no generated key found"); throw new InternalErrorException("Error while
	 * adding task"); }
	 * 
	 * Map<String, Object> keyMap = keys.get(0);
	 * 
	 * return ((Number) keyMap.get("GENERATED_KEY")).longValue(); } catch
	 * (DataAccessException e) { logger.error("Error while adding task", e); throw
	 * new InternalErrorException("Error while adding task"); } }
	 **/

	public void update(long id, TaskEditRequest request) {
		try {
			jdbcTemplate.update(UPDATE, request.getName(), request.getDescription(), request.getStatus(), id);
		} catch (DataAccessException e) {
			logger.error("Error while updating task, e");
			throw new InternalErrorException("Error while updating task");
		}
	}

	public void updateStatus(long id, TaskStatus status) {
		try {
			jdbcTemplate.update(UPDATE_STATUS, status.toString(), id);
		} catch (DataAccessException e) {
			logger.error("Error while updating status, e");
			throw new InternalErrorException("Error while updating status");
		}
	}

	public void updateProject(long id, long projectId) {
		try {
			jdbcTemplate.update(UPDATE_PROJECT, projectId, id);
		} catch (DataAccessException e) {
			logger.error("Error while updating status, e");
			throw new InternalErrorException("Error while updating status");
		}
	}

	public List<Task> getAll() {
		try {
			return jdbcTemplate.query(GET_ALL, taskRowMapper);
		} catch (DataAccessException e) {
			logger.error("Error while getting all task", e);
			throw new InternalErrorException("Error while getting all task");
		}
	}

	public Task getById(long id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID, taskRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			logger.error("Problem with getById task", e);
			throw new InternalErrorException("Project with getById task");
		} catch (DataAccessException e) {
			logger.error("Error while getting task", e);
			throw new InternalErrorException("Error while getting task");
		}
	}

	public List<Task> getAllByUser(long userId) {
		try {
			return jdbcTemplate.query(GET_ALL_BY_USER, taskRowMapper, userId);
		} catch (DataAccessException e) {
			logger.error("Error while getting all task by user", e);
			throw new InternalErrorException("Error while getting all task by user");
		}
	}

	public List<Task> getAllByProject(long projectId) {
		try {
			return jdbcTemplate.query(GET_ALL_BY_PROJECT, taskRowMapper, projectId);
		} catch (DataAccessException e) {
			logger.error("Error while getting all task by project", e);
			throw new InternalErrorException("Error while getting all task by project");
		}
	}

	public void delete(long id) {
		try {
			jdbcTemplate.update(DELETE, id);
		} catch (DataAccessException e) {
			logger.error("Error while deleting task", e);
			throw new InternalErrorException("Error while deleting task");
		}
	}

	public void deleteAllByUser(long userId) {
		try {
			jdbcTemplate.update(DELETE_ALL_BY_USER, userId);
		} catch (DataAccessException e) {
			logger.error("Error while deleting all task by user", e);
			throw new InternalErrorException("Error while deleting all task by user");
		}
	}

	public void deleteAllByProject(long projectId) {
		try {
			jdbcTemplate.update(DELETE_ALL_BY_PROJECT, projectId);
		} catch (DataAccessException e) {
			logger.error("Error while deleting all task by project", e);
			throw new InternalErrorException("Error while deleting all task by project");
		}
	}

}
