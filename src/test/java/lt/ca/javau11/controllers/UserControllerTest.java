package lt.ca.javau11.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.services.UserService;
import lt.ca.javau11.entities.Role;
import lt.ca.javau11.enums.ERole;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(1L, "admin", "admin@email.com", "password", Set.of(new Role(ERole.ADMIN)));
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        List<UserDTO> users = response.getBody();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        
        assertEquals(1, users.size());
        assertEquals(userDTO, users.get(0));
    }


    @Test
    void getUserById_UserExists_ShouldReturnUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));
        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getUserById_UserNotExists_ShouldReturnNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());
        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUser_UserExists_ShouldReturnUpdatedUser() {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(Optional.of(userDTO));
        ResponseEntity<UserDTO> response = userController.updateUser(1L, userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void updateUser_UserNotExists_ShouldReturnNotFound() {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(Optional.empty());
        ResponseEntity<UserDTO> response = userController.updateUser(1L, userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);
        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
