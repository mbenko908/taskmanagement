package sk.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.management.api.ProjectService;
import sk.management.api.request.ProjectAddRequest;
import sk.management.domain.Project;

@RestController
@RequestMapping("project")
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@GetMapping
	public ResponseEntity<List<Project>> getAll(@RequestParam(required = false) Long userId) { // wrapper
		if (userId != null) {
			return ResponseEntity.ok().body(projectService.getAllByUser(userId));
		}
		return ResponseEntity.ok().body(projectService.getAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<Project> getById(@PathVariable("id") long id) {
		return ResponseEntity.ok().body(projectService.get(id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) {
		projectService.delete(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping
	public ResponseEntity<Long> add(@RequestBody ProjectAddRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(projectService.add(request));
	}

}
