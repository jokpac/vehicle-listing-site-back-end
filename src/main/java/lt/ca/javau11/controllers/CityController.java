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
import lt.ca.javau11.entities.City;
import lt.ca.javau11.exceptions.CountryNotFoundException;
import lt.ca.javau11.services.CityService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cities")
public class CityController {


    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    @Operation(summary = "Returns all cities")
    public List<City> getAll() {
        return cityService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{countryId}")
    @Operation(summary = "Creates a new city for a country by id")
    public ResponseEntity<City> addCity(@PathVariable Long countryId, @RequestBody City city) {
        try {
            City createdCity = cityService.addCity(countryId, city);
            return ResponseEntity.status(201).body(createdCity);
        } catch (CountryNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 if the country is not found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a city by id")
    public ResponseEntity<City> getById(@PathVariable Long id) {
        Optional<City> box = cityService.getById(id);
        return ResponseEntity.of(box);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Updates a city by id")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City city) {
        Optional<City> box = cityService.updateCity(id, city);
        return ResponseEntity.of(box);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a city by id")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        boolean isDeleted = cityService.deleteCity(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
