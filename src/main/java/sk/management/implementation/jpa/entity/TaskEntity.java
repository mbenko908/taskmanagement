package sk.management.implementation.jpa.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.management.domain.TaskStatus;

@Entity(name = "task")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

	@Id
	@SequenceGenerator(name = "task_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = true)
	@Setter
	private ProjectEntity project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(nullable = false)
	@Setter
	private String name;

	@Column(nullable = true)
	@Setter
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Setter
	private TaskStatus status;

	@Column(nullable = false)
	private OffsetDateTime createdAt;

	public TaskEntity(UserEntity user, ProjectEntity project, String name, String description, TaskStatus status,
			OffsetDateTime createdAt) {
		this.project = project;
		this.name = name;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
	}

}
