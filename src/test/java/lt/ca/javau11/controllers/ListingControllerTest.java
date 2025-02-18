package lt.ca.javau11.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
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

import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.services.ListingService;

@ExtendWith(MockitoExtension.class)
class ListingControllerTest {

    @Mock
    private ListingService listingService;

    @InjectMocks
    private ListingController listingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(listingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllListings() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.getAllListings()).thenReturn(Collections.singletonList(listingDTO));

        mockMvc.perform(get("/api/listings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(listingService, times(1)).getAllListings();
    }

    @Test
    void testGetListingById() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.getListingById(1L)).thenReturn(Optional.of(listingDTO));

        mockMvc.perform(get("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(listingService, times(1)).getListingById(1L);
    }

    @Test
    void testGetListingById_NotFound() throws Exception {
        when(listingService.getListingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(listingService, times(1)).getListingById(1L);
    }

    @Test
    void testGetUserListings() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.getUserListings(1L)).thenReturn(Collections.singletonList(listingDTO));

        mockMvc.perform(get("/api/listings/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(listingService, times(1)).getUserListings(1L);
    }

    @Test
    void testCreateListing() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.createListing(any(ListingDTO.class))).thenReturn(listingDTO);

        mockMvc.perform(post("/api/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(listingService, times(1)).createListing(any(ListingDTO.class));
    }

    @Test
    void testUpdateListing() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.updateListing(eq(1L), any(ListingDTO.class))).thenReturn(Optional.of(listingDTO));

        mockMvc.perform(put("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(listingService, times(1)).updateListing(eq(1L), any(ListingDTO.class));
    }

    @Test
    void testUpdateListing_NotFound() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.updateListing(eq(1L), any(ListingDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listingDTO)))
                .andExpect(status().isNotFound());

        verify(listingService, times(1)).updateListing(eq(1L), any(ListingDTO.class));
    }


    @Test
    void testUpdateListingStatus() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.updateListingStatus(1L, ListingStatus.ACTIVE)).thenReturn(Optional.of(listingDTO));

        mockMvc.perform(put("/api/listings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ACTIVE\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(listingService, times(1)).updateListingStatus(1L, ListingStatus.ACTIVE);
    }

    @Test
    void testUpdateListingStatus_NotFound() throws Exception {
        when(listingService.updateListingStatus(1L, ListingStatus.ACTIVE)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/listings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ACTIVE\""))
                .andExpect(status().isNotFound());

        verify(listingService, times(1)).updateListingStatus(1L, ListingStatus.ACTIVE);
    }

    @Test
    void testDeleteListing() throws Exception {
        when(listingService.deleteListing(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(listingService, times(1)).deleteListing(1L);
    }

    @Test
    void testDeleteListing_NotFound() throws Exception {
        when(listingService.deleteListing(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/listings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(listingService, times(1)).deleteListing(1L);
    }

    @Test
    void testGetFilteredListings() throws Exception {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(1L);

        when(listingService.getFilteredListings(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(listingDTO));

        mockMvc.perform(get("/api/listings/filter")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(listingService, times(1)).getFilteredListings(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}