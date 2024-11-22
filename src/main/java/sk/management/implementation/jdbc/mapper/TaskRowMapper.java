package sk.management.implementation.jdbc.mapper;

import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import sk.management.domain.Task;
import sk.management.domain.TaskStatus;

@Component
public class TaskRowMapper implements RowMapper<Task> {
	@Override
	public Task mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
		return new Task(rs.getLong("id"), rs.getLong("user_id"),
				rs.getObject("project_id") != null ? rs.getLong("project_id") : null, rs.getString("name"),
				rs.getString("description"), TaskStatus.fromString(rs.getString("status")),
				rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC));
	}
}
