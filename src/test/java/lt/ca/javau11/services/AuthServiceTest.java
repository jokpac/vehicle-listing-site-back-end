package lt.ca.javau11.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private User user;
    private Role userRole;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setPassword("newpassword");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setRole(Set.of("USER"));

        user = new User("testuser", "encodedPassword", "test@example.com");

        userRole = new Role(ERole.USER);
        
        userDTO = new UserDTO(1L, "admin", "admin@email.com", "password", Set.of(new Role(ERole.USER)));
    }

    @Test
    void authenticateUser_ShouldReturnJwtResponse() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDTO);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        JwtResponse response = authService.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("admin", response.getUsername());
    }

    @Test
    void registerUser_ShouldReturnSuccessMessage() {
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(user);

        MessageResponse response = authService.registerUser(signupRequest);

        assertNotNull(response);
        assertEquals("User registered successfully!", response.getMessage());
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> authService.registerUser(signupRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Error: Username is already taken!", exception.getReason());
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> authService.registerUser(signupRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Error: Email is already in use!", exception.getReason());
    }
}
