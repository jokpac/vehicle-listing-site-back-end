package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lt.ca.javau11.entities.User;
import lt.ca.javau11.models.UserDTO;
import lt.ca.javau11.repositories.UserRepository;
import lt.ca.javau11.utils.EntityMapper;

@Service
public class UserService implements UserDetailsService  {

	private final UserRepository userRepository;
	private final EntityMapper entityMapper;
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	
	public UserService(UserRepository userRepository, EntityMapper entityMapper) {
		this.userRepository = userRepository;
		this.entityMapper = entityMapper;
	}
	
	public UserDTO createUser(UserDTO userDto) {
		User userBeforeSave = entityMapper.toUser(userDto);

		User userAfterSave = userRepository.save(userBeforeSave);		
		
		return entityMapper.toUserDto(userAfterSave);		
	}
	
	public List<UserDTO> getAllUsers(){
		List<User> users = userRepository.findAll();
		
		return users.stream()
				.map(entityMapper::toUserDto)
				.toList();
	}
	
	public Optional<UserDTO> getUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.map(entityMapper::toUserDto);
	}
	
	public Optional<UserDTO> updateUser(Long id, UserDTO userDto ){
		
		if( userRepository.existsById(id) ) {
			User userEntityBeforeSave = entityMapper.toUser(userDto);
			userEntityBeforeSave.setId(id);
			
			User userEntityAfterSave = userRepository.save(userEntityBeforeSave);
			return Optional.of( entityMapper.toUserDto(userEntityAfterSave));
			
		} else {
			return Optional.empty();
		}
		
	}
	
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository
							.findByUsername(username)
							.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		
		logger.info("Loaded :" + user.toString());
		return entityMapper.toUserDto(user);
	}
	
}
