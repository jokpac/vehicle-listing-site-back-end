package lt.ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.entities.Model;

public interface ModelRepository extends JpaRepository<Model, Long> {

}
