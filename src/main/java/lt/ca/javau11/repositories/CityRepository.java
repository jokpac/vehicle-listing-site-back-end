package lt.ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau11.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
