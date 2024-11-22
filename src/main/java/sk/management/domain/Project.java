package sk.management.domain;

import java.time.OffsetDateTime;

import lombok.Value;

@Value
public class Project {

	private final long Id;
	private final long userId;
	private final String name;
	private final String description;
	private final OffsetDateTime createAt;
}
