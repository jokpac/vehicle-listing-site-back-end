package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.exceptions.CountryNotFoundException;
import lt.ca.javau11.repositories.CityRepository;
import lt.ca.javau11.repositories.CountryRepository;

@Service
public class CityService {


    private final CityRepository repo;
    private final CountryRepository countryRepo;

    public CityService(CityRepository repo, CountryRepository countryRepo) {
        this.repo = repo;
        this.countryRepo = countryRepo;
    }

    public List<City> getAll() {
        return repo.findAll();
    }

    public City addCity(Long countryId, City city) {
        Optional<Country> countryBox = countryRepo.findById(countryId);
        if (countryBox.isPresent()) {
            Country country = countryBox.get();
            city.setCountry(country);
            return repo.save(city);
        }
        throw new CountryNotFoundException("Country with ID " + countryId + " not found");
    }

    public Optional<City> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<City> updateCity(Long id, City cityDetails) {
        Optional<City> box = repo.findById(id);
        if (box.isPresent()) {
            City existingCity = box.get();
            existingCity.setName(cityDetails.getName());
            return Optional.of(repo.save(existingCity));
        }
        return Optional.empty();
    }

    public boolean deleteCity(Long id) {
        Optional<City> box = repo.findById(id);
        if (box.isEmpty()) {
            return false;
        }
        repo.delete(box.get());
        return true;
    }
}
