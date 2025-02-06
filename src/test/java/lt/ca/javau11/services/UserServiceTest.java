package lt.ca.javau11.services;

import lt.ca.javau11.entities.User;
import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.repositories.UserRepository;
import lt.ca.javau11.utils.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private Long userId = 1L;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
    }

    @Test
    public void testCreateUser() {
        when(entityMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(entityMapper.toUserDto(user)).thenReturn(userDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    public void testGetUserById_Found() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(entityMapper.toUserDto(user)).thenReturn(userDTO);

        Optional<UserDTO> foundUser = userService.getUserById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDTO> foundUser = userService.getUserById(userId);

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testUpdateUser_Success() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(entityMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(userDTO);

        Optional<UserDTO> updatedUser = userService.updateUser(userId, userDTO);

        assertTrue(updatedUser.isPresent());
        assertEquals("testuser", updatedUser.get().getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_NotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        Optional<UserDTO> updatedUser = userService.updateUser(userId, userDTO);

        assertFalse(updatedUser.isPresent());
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testLoadUserByUsername_Found() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(entityMapper.toUserDto(user)).thenReturn(userDTO);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testuser");
        });
    }
}
