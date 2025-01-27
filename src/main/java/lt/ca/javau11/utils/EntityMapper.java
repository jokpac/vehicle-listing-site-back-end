package lt.ca.javau11.utils;

import org.springframework.stereotype.Component;

import lt.ca.javau11.entities.User;
import lt.ca.javau11.models.UserDTO;

@Component
public class EntityMapper {
	
	public User toUser(UserDTO dto) {
		
		User user = new User();
		user.setId(dto.getId());
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());	
		return user;		
	}
	
	public UserDTO toUserDto(User user) {
		
		return new UserDTO( 
				user.getId(), 
				user.getUsername(), 
				user.getEmail(), 
				user.getPassword(), 
				user.getRoles() 
			);
	}
	
}