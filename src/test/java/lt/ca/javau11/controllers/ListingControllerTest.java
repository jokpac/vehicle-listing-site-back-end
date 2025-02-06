package lt.ca.javau11.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.ca.javau11.entities.Listing;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.services.ListingService;

@ExtendWith(MockitoExtension.class)
class ListingControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private ListingService listingService;
    
    @InjectMocks
    private ListingController listingController;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(listingController).build();
    }

    @Test
    void testGetAllListings() throws Exception {
        when(listingService.getAllListings()).thenReturn(List.of(new Listing()));
        
        mockMvc.perform(get("/api/listings"))
                .andExpect(status().isOk());
        
        verify(listingService).getAllListings();
    }

    @Test
    void testGetListingById_Found() throws Exception {
        Listing listing = new Listing();
        listing.setId(1L);
        when(listingService.getListingById(1L)).thenReturn(Optional.of(listing));
        
        mockMvc.perform(get("/api/listings/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetListingById_NotFound() throws Exception {
        when(listingService.getListingById(1L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/listings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateListing_Success() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        Listing listing = new Listing();
        listing.setId(1L);
        
        when(listingService.createListing(any(ListingDTO.class))).thenReturn(listing);
        
        mockMvc.perform(post("/api/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateListing_Found() throws Exception {
        ListingDTO updatedListing = new ListingDTO();
        Listing listing = new Listing();
        listing.setId(1L);
        
        when(listingService.updateListing(eq(1L), any(ListingDTO.class))).thenReturn(Optional.of(listing));
        
        mockMvc.perform(put("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedListing)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateListing_NotFound() throws Exception {
        when(listingService.updateListing(eq(1L), any(ListingDTO.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(put("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ListingDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateListingStatus_Found() throws Exception {
        Listing listing = new Listing();
        listing.setId(1L);
        
        when(listingService.updateListingStatus(eq(1L), any(ListingStatus.class))).thenReturn(Optional.of(listing));
        
        mockMvc.perform(put("/api/listings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ListingStatus.ACTIVE)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateListingStatus_NotFound() throws Exception {
        when(listingService.updateListingStatus(eq(1L), any(ListingStatus.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(put("/api/listings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ListingStatus.ACTIVE)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteListing_Success() throws Exception {
        when(listingService.deleteListing(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/api/listings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteListing_NotFound() throws Exception {
        when(listingService.deleteListing(1L)).thenReturn(false);
        
        mockMvc.perform(delete("/api/listings/1"))
                .andExpect(status().isNotFound());
    }
}
