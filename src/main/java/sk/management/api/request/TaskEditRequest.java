package sk.management.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.management.domain.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEditRequest {
	private String name;
	private String description;
	private TaskStatus status;
}
