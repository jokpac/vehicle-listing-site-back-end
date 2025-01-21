package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lt.ca.javau11.entities.Country;
import lt.ca.javau11.models.CityDTO;
import lt.ca.javau11.repositories.CountryRepository;

@Service
public class CountryService {

    private final CountryRepository repo;

    public CountryService(CountryRepository repo) {
        this.repo = repo;
    }

    // Retrieve all countries
    public List<Country> getAll() {
        return repo.findAll();
    }

    // Add a new country
    public Country addCountry(Country country) {
        return repo.save(country);
    }

    // Get a country by ID
    public Optional<Country> getById(Long id) {
        return repo.findById(id);
    }

    // Get cities by country ID
    public Optional<List<CityDTO>> getCitiesByCountry(Long countryId) {
        Optional<Country> box = repo.findById(countryId);
        if (box.isPresent()) {
            List<CityDTO> cityDTOs = box.get()
                .getCities()
                .stream()
                .map(city -> new CityDTO(city.getId(), city.getName()))
                .collect(Collectors.toList());
            return Optional.of(cityDTOs);
        }
        return Optional.empty();
    }
    
    // Update a country
    public Optional<Country> updateCountry(Long id, Country countryDetails) {
        Optional<Country> box = repo.findById(id);
        if (box.isPresent()) {
            Country existingCountry = box.get();
            existingCountry.setName(countryDetails.getName());
            return Optional.of(repo.save(existingCountry));
        }
        return Optional.empty();
    }

    // Delete a country
    public boolean deleteCountry(Long id) {
        Optional<Country> box = repo.findById(id);
        if (box.isEmpty())
            return false;
        repo.delete(box.get());
        return true;
    }
}
