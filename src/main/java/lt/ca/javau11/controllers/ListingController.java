package lt.ca.javau11.controllers;

import java.util.List;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.validation.Valid;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.entities.Listing;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.services.ListingService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);
    
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
    
    @GetMapping("/user/{id}")
    public List<Listing> getUserListings(@PathVariable Long id) {
    	return listingService.getUserListings(id);

    }
    
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody ListingDTO listingRequest) {
        logger.info("Received request to create a listing with data: {}", listingRequest);

        try {
            Listing createdListing = listingService.createListing(listingRequest);
            logger.info("Successfully created a listing with ID: {}", createdListing.getId());
            return ResponseEntity.status(201).body(createdListing);
        } catch (Exception e) {
            logger.error("Error occurred while creating a listing: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("@listingService.isListingOwner(#id, authentication.name)")
    @PutMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable Long id, @Valid @RequestBody ListingDTO updatedListing) {
 
        Optional<Listing> box = listingService.updateListing(id, updatedListing);
        return ResponseEntity.of(box);
    }
    
    @PreAuthorize("@listingService.isListingOwner(#id, authentication.name)")
    @PutMapping("/{id}/status")
    public ResponseEntity<Listing> updateListingStatus(@PathVariable Long id, @RequestBody ListingStatus status) {
        Optional<Listing> updatedListing = listingService.updateListingStatus(id, status);
        return updatedListing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or @listingService.isListingOwner(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        boolean isDeleted = listingService.deleteListing(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}