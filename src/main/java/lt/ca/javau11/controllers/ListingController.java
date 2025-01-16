package lt.ca.javau11.controllers;

import java.util.List;


import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.entities.Listing;
import lt.ca.javau11.services.ListingService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        Optional<Listing> box = listingService.getListingById(id);
        return ResponseEntity.of(box);
    }

    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody @Valid ListingDTO listingRequest) {
        Listing createdListing = listingService.createListing(listingRequest);
        return ResponseEntity.status(201).body(createdListing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable Long id,
                                                 @RequestBody Listing updatedListing,
                                                 @RequestParam Long countryId,
                                                 @RequestParam Long cityId,
                                                 @RequestParam Long makeId,
                                                 @RequestParam Long modelId
                                                 ) {
    	
        Optional<Listing> box = listingService.updateListing(id, updatedListing, countryId, cityId, makeId, modelId);
        return ResponseEntity.of(box);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        boolean isDeleted = listingService.deleteListing(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
