package lt.ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau11.entities.Country;
import lt.ca.javau11.services.CountryService;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // Get all countries
    @GetMapping
    public List<Country> getAll() {
        return countryService.getAll();
    }

    // Add a new country
    @PostMapping
    public Country addCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    // Get a country by ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable Long id) {
        Optional<Country> box = countryService.getById(id);
        return ResponseEntity.of(box);
    }
    
    // Get cities by country ID
    @GetMapping("/{id}/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(@PathVariable Long id) {
        Optional<List<String>> cities = countryService.getCitiesByCountry(id);
        return ResponseEntity.of(cities);
    }

    // Update a country
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @RequestBody Country country) {
        Optional<Country> box = countryService.updateCountry(id, country);
        return ResponseEntity.of(box);
    }

    // Delete a country
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        boolean isDeleted = countryService.deleteCountry(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}