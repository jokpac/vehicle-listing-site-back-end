package lt.ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
