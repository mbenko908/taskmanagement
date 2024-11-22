package sk.management.implementation.jdbc.mapper;

import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import sk.management.domain.Project;

@Component
public class ProjectRowMapper implements RowMapper<Project> {
	@Override
	public Project mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
		return new Project(rs.getLong("id"), rs.getLong("user_id"), rs.getString("name"), rs.getString("description"),
				rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC));
	}
}
