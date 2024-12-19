package sk.management.domain;

import java.time.OffsetDateTime;

import lombok.Value;

@Value
public class Task {
	long taskId;
	long userId;
	Long projectId;
	String name;
	String description;
	TaskStatus status;
	OffsetDateTime createdAt;
}