package lt.ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.entities.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
