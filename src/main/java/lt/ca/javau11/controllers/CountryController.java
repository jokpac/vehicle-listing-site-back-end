package lt.ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.models.CityDTO;
import lt.ca.javau11.services.CountryService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Returns all coutries with cities")
    public List<Country> getAll() {
        return countryService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Creates a new country")
    public Country addCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a country by id with its cities")
    public ResponseEntity<Country> getById(@PathVariable Long id) {
        Optional<Country> box = countryService.getById(id);
        return ResponseEntity.of(box);
    }
    
    @GetMapping("/{id}/cities")
    @Operation(summary = "Returns cities by country id")
    public ResponseEntity<List<CityDTO>> getCitiesByCountry(@PathVariable Long id) {
        Optional<List<CityDTO>> cities = countryService.getCitiesByCountry(id);
        return cities.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Updates a country by id")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @RequestBody Country country) {
        Optional<Country> box = countryService.updateCountry(id, country);
        return ResponseEntity.of(box);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a country by id")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        boolean isDeleted = countryService.deleteCountry(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}