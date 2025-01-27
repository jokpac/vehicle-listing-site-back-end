package lt.ca.javau11.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/mod")
	public ResponseEntity<String> getModePage(){		
		return new ResponseEntity<>("Moderator page is accessible", HttpStatus.OK);	
	}
	
	@GetMapping("/user")
	public ResponseEntity<String> getUserPage(){		
		return new ResponseEntity<>("User page is accessible", HttpStatus.OK);	
	}
	
	@GetMapping("/admin")
	public ResponseEntity<String> getAdminPage(){		
		return new ResponseEntity<>("Admin page is accessible", HttpStatus.OK);	
	}
	
	@GetMapping("/all")
	public ResponseEntity<String> getHomePage(){		
		return new ResponseEntity<>("Home page is accessible", HttpStatus.OK);	
	}
	
}