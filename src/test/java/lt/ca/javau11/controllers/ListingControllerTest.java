package lt.ca.javau11.controllers;

import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.services.ListingService;
import lt.ca.javau11.enums.ListingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingControllerTest {

    @Mock
    private ListingService listingService;

    @InjectMocks
    private ListingController listingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllListings_ShouldReturnAllListings() {
        ListingDTO listing1 = new ListingDTO();
        ListingDTO listing2 = new ListingDTO();
        when(listingService.getAllListings()).thenReturn(Arrays.asList(listing1, listing2));

        List<ListingDTO> result = listingController.getAllListings();

        assertEquals(2, result.size());
        verify(listingService, times(1)).getAllListings();
    }

    @Test
    void getListingById_ShouldReturnListing_WhenListingExists() {
        Long id = 1L;
        ListingDTO listing = new ListingDTO();
        listing.setId(id);
        when(listingService.getListingById(id)).thenReturn(Optional.of(listing));

        ResponseEntity<ListingDTO> response = listingController.getListingById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ListingDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(id, body.getId());
        verify(listingService, times(1)).getListingById(id);
    }


    @Test
    void getListingById_ShouldReturnNotFound_WhenListingDoesNotExist() {
        Long id = 1L;
        when(listingService.getListingById(id)).thenReturn(Optional.empty());

        ResponseEntity<ListingDTO> response = listingController.getListingById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(listingService, times(1)).getListingById(id);
    }

    @Test
    void getUserListings_ShouldReturnListingsForUser() {
        Long userId = 1L;
        ListingDTO listing1 = new ListingDTO();
        ListingDTO listing2 = new ListingDTO();
        when(listingService.getUserListings(userId)).thenReturn(Arrays.asList(listing1, listing2));

        List<ListingDTO> result = listingController.getUserListings(userId);

        assertEquals(2, result.size());
        verify(listingService, times(1)).getUserListings(userId);
    }

    @Test
    void createListing_ShouldCreateListing_WhenRequestIsValid() {
        ListingDTO listingRequest = new ListingDTO();
        listingRequest.setTitle("Test Listing");
        
        ListingDTO createdListing = new ListingDTO();
        createdListing.setId(1L);
        createdListing.setTitle("Test Listing");
        
        when(listingService.createListing(listingRequest)).thenReturn(createdListing);

        ResponseEntity<ListingDTO> response = listingController.createListing(listingRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ListingDTO body = response.getBody();
        assertNotNull(body, "Response body should not be null");

        assertEquals(1L, body.getId());
        assertEquals("Test Listing", body.getTitle());

        verify(listingService, times(1)).createListing(listingRequest);
    }


    @Test
    void updateListing_ShouldUpdateListing_WhenListingExists() {
        Long id = 1L;
        ListingDTO updatedListing = new ListingDTO();
        updatedListing.setTitle("Updated Listing");
        
        when(listingService.updateListing(id, updatedListing)).thenReturn(Optional.of(updatedListing));

        ResponseEntity<ListingDTO> response = listingController.updateListing(id, updatedListing);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ListingDTO body = response.getBody();
        assertNotNull(body);

        assertEquals("Updated Listing", body.getTitle());

        verify(listingService, times(1)).updateListing(id, updatedListing);
    }


    @Test
    void updateListing_ShouldReturnNotFound_WhenListingDoesNotExist() {
        Long id = 1L;
        ListingDTO updatedListing = new ListingDTO();
        when(listingService.updateListing(id, updatedListing)).thenReturn(Optional.empty());

        ResponseEntity<ListingDTO> response = listingController.updateListing(id, updatedListing);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(listingService, times(1)).updateListing(id, updatedListing);
    }

    @Test
    void updateListingStatus_ShouldUpdateStatus_WhenListingExists() {
        Long id = 1L;
        ListingStatus status = ListingStatus.ACTIVE;
        ListingDTO updatedListing = new ListingDTO();
        updatedListing.setListingStatus(status);
        when(listingService.updateListingStatus(id, status)).thenReturn(Optional.of(updatedListing));

        ResponseEntity<ListingDTO> response = listingController.updateListingStatus(id, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ListingDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(status, body.getListingStatus());
        verify(listingService, times(1)).updateListingStatus(id, status);
    }

    @Test
    void updateListingStatus_ShouldReturnNotFound_WhenListingDoesNotExist() {
        Long id = 1L;
        ListingStatus status = ListingStatus.ACTIVE;
        when(listingService.updateListingStatus(id, status)).thenReturn(Optional.empty());

        ResponseEntity<ListingDTO> response = listingController.updateListingStatus(id, status);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(listingService, times(1)).updateListingStatus(id, status);
    }

    @Test
    void deleteListing_ShouldDeleteListing_WhenListingExists() {
        Long id = 1L;
        when(listingService.deleteListing(id)).thenReturn(true);

        ResponseEntity<Void> response = listingController.deleteListing(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(listingService, times(1)).deleteListing(id);
    }

    @Test
    void deleteListing_ShouldReturnNotFound_WhenListingDoesNotExist() {
        Long id = 1L;
        when(listingService.deleteListing(id)).thenReturn(false);

        ResponseEntity<Void> response = listingController.deleteListing(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(listingService, times(1)).deleteListing(id);
    }

    @Test
    void getFilteredListings_ShouldReturnFilteredListings() {
        Double priceMin = 1000.0;
        Double priceMax = 5000.0;
        ListingDTO listing1 = new ListingDTO();
        ListingDTO listing2 = new ListingDTO();
        when(listingService.getFilteredListings(priceMin, priceMax, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
                .thenReturn(Arrays.asList(listing1, listing2));

        List<ListingDTO> result = listingController.getFilteredListings(priceMin, priceMax, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertEquals(2, result.size());
        verify(listingService, times(1)).getFilteredListings(priceMin, priceMax, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}