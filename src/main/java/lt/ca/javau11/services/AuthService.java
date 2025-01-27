package lt.ca.javau11.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lt.ca.javau11.entities.Role;
import lt.ca.javau11.entities.User;
import lt.ca.javau11.enums.ERole;
import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.payload.requests.LoginRequest;
import lt.ca.javau11.payload.requests.SignupRequest;
import lt.ca.javau11.payload.responses.JwtResponse;
import lt.ca.javau11.payload.responses.MessageResponse;
import lt.ca.javau11.repositories.RoleRepository;
import lt.ca.javau11.repositories.UserRepository;
import lt.ca.javau11.security.jwt.JwtUtils;

@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	  AuthenticationManager authenticationManager;

	  UserRepository userRepository;

	  RoleRepository roleRepository;

	  PasswordEncoder encoder;

	  JwtUtils jwtUtils;
	  
	  public AuthService (AuthenticationManager authenticationManager,
			  UserRepository userRepository,
			  RoleRepository roleRepository,
			  PasswordEncoder encoder,
			  JwtUtils jwtUtils) {
		  this.authenticationManager = authenticationManager;
		  this.userRepository = userRepository;
		  this.roleRepository = roleRepository;
		  this.encoder = encoder;
		  this.jwtUtils = jwtUtils;
	  }

	public JwtResponse authenticateUser(LoginRequest loginRequest) {
       
		Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
		
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDTO userDetails = (UserDTO) authentication.getPrincipal();
        logger.info("Before: " + userDetails.toString());
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }
	
	public MessageResponse registerUser(SignupRequest signUpRequest) {
		
        checkUserExists(signUpRequest);
        
        User user = createNewUser(signUpRequest);
        Set<Role> roles = getInitialRoles(signUpRequest);

        user.setRoles(roles);
       
        logger.info("Before: " + user.toString());
        user = userRepository.save(user);
        logger.info("After: " + user.toString());
        
        return new MessageResponse("User registered successfully!");
    }

	private User createNewUser(SignupRequest signUpRequest) {
		User user = new User(
				signUpRequest.getUsername(), 
				encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());
		logger.info(user.toString());
		return user;
	}

	private Set<Role> getInitialRoles(SignupRequest signUpRequest) {
		Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                Role resolvedRole = roleRepository.findByName(ERole.valueOf(role.toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(resolvedRole);
            }
        }
		return roles;
	}

	private void checkUserExists(SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Email is already in use!");
        }
	}
}
