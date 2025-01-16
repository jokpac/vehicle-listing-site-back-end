package lt.ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.entities.User;

public interface UserRepository extends JpaRepository <User, Long>{

}
