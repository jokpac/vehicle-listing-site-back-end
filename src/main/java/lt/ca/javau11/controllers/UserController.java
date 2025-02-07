package lt.ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	@Operation(summary = "Creates a new user")
	public ResponseEntity<UserDTO>  createUser(@RequestBody UserDTO userDto) {
		UserDTO createdUser = userService.createUser(userDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);		
	}
	
	@GetMapping
	@Operation(summary = "Returns all users")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		List<UserDTO> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);	
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Returns user by id")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){	
		Optional<UserDTO> userInBox = userService.getUserById(id);
		return userInBox
				.map( ResponseEntity::ok )
				.orElseGet( () -> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Updates user by id")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto){
		Optional<UserDTO> userInBox = userService.updateUser(id, userDto);
		
		return userInBox
				.map( ResponseEntity::ok )
				.orElseGet( () -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Deletes user by id")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id){
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
