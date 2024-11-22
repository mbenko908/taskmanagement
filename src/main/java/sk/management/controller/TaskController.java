package sk.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.management.api.TaskService;
import sk.management.api.exception.TaskAssignStatusRequest;
import sk.management.api.exception.TaskChangeStatusRequest;
import sk.management.api.request.TaskAddRequest;
import sk.management.api.request.TaskEditRequest;
import sk.management.domain.Task;

@RestController
@RequestMapping("task")
public class TaskController {

	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Task>> getAll(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long projectId) {

		if (userId != null) {
			return ResponseEntity.ok().body(service.getAllByUserId(userId));
		} else if (projectId != null) {
			return ResponseEntity.ok().body(service.getAllByProjectId(projectId));
		}
		return ResponseEntity.ok().body(service.getAll());
	}

	@PostMapping
	public ResponseEntity<Long> add(@RequestBody TaskAddRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.add(request));
	}

	@PutMapping("{id}")
	public ResponseEntity<Void> edit(@PathVariable("id") long id, @RequestBody TaskEditRequest request) {
		service.edit(id, request);
		return ResponseEntity.ok().build();
	}

	@PutMapping("{id}/status")
	public ResponseEntity<Void> changeStatus(@PathVariable("id") long id,
			@RequestBody TaskChangeStatusRequest request) {
		service.changeStatus(id, request.getStatus());
		return ResponseEntity.ok().build();
	}

	@PutMapping("{id}/assign")
	public ResponseEntity<Void> changeStatus(@PathVariable("id") long id,
			@RequestBody TaskAssignStatusRequest request) {
		service.assignProject(id, request.getProjectId());
		return ResponseEntity.ok().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<Task> getById(@PathVariable("id") long id) {
		return ResponseEntity.ok().body(service.get(id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

}
