package lt.ca.javau11.models;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lt.ca.javau11.entities.Role;

public class UserDTO implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String email;

	private Set<Role> roles;

	@JsonIgnore
	private String password;

	public UserDTO() {}

	public UserDTO(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public UserDTO(Long id, String username, String email, String password, Set<Role> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public UserDTO(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDTO user = (UserDTO) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", username=" + username + ", email=" + email + ", roles=" + roles + ", password="
				+ password + "]";
	}
}
