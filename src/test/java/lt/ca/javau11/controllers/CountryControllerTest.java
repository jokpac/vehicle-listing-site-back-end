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

import lt.ca.javau11.entities.Country;
import lt.ca.javau11.models.CityDTO;
import lt.ca.javau11.services.CountryService;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Country country;
    private Long countryId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();

        country = new Country();
        country.setId(countryId);
        country.setName("Test Country");
    }

    @Test
    void testGetAllCountries() throws Exception {
        when(countryService.getAll()).thenReturn(List.of(country));

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Country"));
    }

    @Test
    void testAddCountry() throws Exception {
        when(countryService.addCountry(any(Country.class))).thenReturn(country);

        mockMvc.perform(post("/api/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Country"));
    }

    @Test
    void testGetCountryById_Found() throws Exception {
        when(countryService.getById(countryId)).thenReturn(Optional.of(country));

        mockMvc.perform(get("/api/countries/{id}", countryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Country"));
    }

    @Test
    void testGetCountryById_NotFound() throws Exception {
        when(countryService.getById(countryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/countries/{id}", countryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCitiesByCountry_Found() throws Exception {
        CityDTO cityDTO = new CityDTO(1L, "Test City");
        when(countryService.getCitiesByCountry(countryId)).thenReturn(Optional.of(List.of(cityDTO)));

        mockMvc.perform(get("/api/countries/{id}/cities", countryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test City"));
    }

    @Test
    void testGetCitiesByCountry_NotFound() throws Exception {
        when(countryService.getCitiesByCountry(countryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/countries/{id}/cities", countryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCountry_Success() throws Exception {
        when(countryService.updateCountry(eq(countryId), any(Country.class))).thenReturn(Optional.of(country));

        mockMvc.perform(put("/api/countries/{id}", countryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Country"));
    }

    @Test
    void testUpdateCountry_NotFound() throws Exception {
        when(countryService.updateCountry(eq(countryId), any(Country.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/countries/{id}", countryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCountry_Success() throws Exception {
        when(countryService.deleteCountry(countryId)).thenReturn(true);

        mockMvc.perform(delete("/api/countries/{id}", countryId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCountry_NotFound() throws Exception {
        when(countryService.deleteCountry(countryId)).thenReturn(false);

        mockMvc.perform(delete("/api/countries/{id}", countryId))
                .andExpect(status().isNotFound());
    }
}
