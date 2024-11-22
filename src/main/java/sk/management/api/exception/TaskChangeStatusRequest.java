package sk.management.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.management.domain.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskChangeStatusRequest {
	private TaskStatus status;
}
