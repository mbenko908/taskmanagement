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
import org.springframework.web.bind.annotation.RestController;

import sk.management.api.UserService;
import sk.management.api.request.UserAddRequest;
import sk.management.domain.User;

@RestController
@RequestMapping("user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> getAll() { // wrapper
		return ResponseEntity.ok().body(userService.getAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<User> getById(@PathVariable("id") long id) {
		return ResponseEntity.ok().body(userService.get(id));
	}

	@PostMapping
	public ResponseEntity<Long> add(@RequestBody UserAddRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(request));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) {
		userService.delete(id);
		return ResponseEntity.ok().build();
	}

}
