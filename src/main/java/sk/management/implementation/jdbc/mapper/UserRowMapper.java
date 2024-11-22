package sk.management.implementation.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import sk.management.domain.User;

@Component
public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
	}
}
