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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lt.ca.javau11.models.ListingDTO;
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
    @Operation(summary = "Returns all listings")
    public List<ListingDTO> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a listing by id")
    public ResponseEntity<ListingDTO> getListingById(@PathVariable Long id) {
        Optional<ListingDTO> box = listingService.getListingById(id);
        return ResponseEntity.of(box);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Returns listings of a user by id")
    public List<ListingDTO> getUserListings(@PathVariable Long userId) {
    	return listingService.getUserListings(userId);

    }
    
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "Creates a new listing")
    public ResponseEntity<ListingDTO> createListing(@Valid @RequestBody ListingDTO listingRequest) {
        logger.info("Received request to create a listing with data: {}", listingRequest);
        ListingDTO createdListing = listingService.createListing(listingRequest);
        logger.info("Successfully created a listing with ID: {}", createdListing.getId());
        return ResponseEntity.status(201).body(createdListing);
    }

    @PreAuthorize("@listingService.isListingOwner(#id, authentication.name)")
    @PutMapping("/{id}")
    @Operation(summary = "Updates a listing by id")
    public ResponseEntity<ListingDTO> updateListing(@PathVariable Long id, @Valid @RequestBody ListingDTO updatedListing) {
 
        Optional<ListingDTO> box = listingService.updateListing(id, updatedListing);
        return ResponseEntity.of(box);
    }
    
    @PreAuthorize("@listingService.isListingOwner(#id, authentication.name)")
    @PutMapping("/{id}/status")
    @Operation(summary = "Changes listing status (ACTIVE/INACTIVE)")
    public ResponseEntity<ListingDTO> updateListingStatus(@PathVariable Long id, @RequestBody ListingStatus status) {
        Optional<ListingDTO> updatedListing = listingService.updateListingStatus(id, status);
        return updatedListing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or @listingService.isListingOwner(#id, authentication.name)")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a listing by id")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        boolean isDeleted = listingService.deleteListing(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/filter")
    @Operation(summary = "Returns filtered listings")
    public List<ListingDTO> getFilteredListings(
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) Integer mileageMin,
            @RequestParam(required = false) Integer mileageMax,
            @RequestParam(required = false) Double engineSizeMin,
            @RequestParam(required = false) Double engineSizeMax,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String drivenWheels,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Long makeId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) String listingStatus,
            @RequestParam(required = false) String listingType) {

        return listingService.getFilteredListings(
                priceMin, priceMax, yearMin, yearMax,
                mileageMin, mileageMax, engineSizeMin, engineSizeMax,
                fuelType, transmission, drivenWheels,
                countryId, cityId, makeId, modelId, listingStatus, listingType);
    }

}