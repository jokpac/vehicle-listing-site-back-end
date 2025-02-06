package lt.ca.javau11.controllers;

import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.services.CityService;
import lt.ca.javau11.exceptions.CountryNotFoundException;
import lt.ca.javau11.models.CityDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private City city;
    private Long cityId = 1L;
    private Long countryId = 2L;
    private Country country;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();

        country = new Country();
        country.setId(countryId);
        country.setName("Test Country");

        city = new City();
        city.setId(cityId);
        city.setName("Test City");
        city.setCountry(country);
    }

    @Test
    public void testGetAllCities() throws Exception {
    	
        when(cityService.getAll()).thenReturn(List.of(city));

        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test City"));
    }

    @Test
    void testAddCitySuccess() throws Exception {
        Country country = new Country();
        country.setId(countryId);
        country.setName("Test Country");

        CityDTO cityDTO = new CityDTO(null, "New City");

        when(cityService.addCity(eq(countryId), any(City.class))).thenReturn(new City(cityId, cityDTO.getName(), country));

        mockMvc.perform(post("/api/cities/{countryId}", countryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New City"));
    }

    @Test
    void testAddCity_CountryNotFound() throws Exception {
        CityDTO cityDTO = new CityDTO(null, "New City");

        when(cityService.addCity(anyLong(), any(City.class))).thenThrow(new CountryNotFoundException("Country not found"));

        mockMvc.perform(post("/api/cities/{countryId}", countryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCityById() throws Exception {
    	
        when(cityService.getById(cityId)).thenReturn(Optional.of(city));

        mockMvc.perform(get("/api/cities/{id}", cityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test City"))
                .andExpect(jsonPath("$.id").value(cityId));
    }

    @Test
    public void testGetCityByIdNotFound() throws Exception {
    	
        when(cityService.getById(cityId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cities/{id}", cityId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCitySuccess() throws Exception {
        City updatedCity = new City();
        updatedCity.setId(cityId);
        updatedCity.setName("Updated City");
        updatedCity.setCountry(country);

        when(cityService.updateCity(eq(cityId), any(City.class))).thenReturn(Optional.of(updatedCity));

        mockMvc.perform(put("/api/cities/{id}", cityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated City"));
    }

    @Test
    public void testUpdateCityNotFound() throws Exception {
    	
        when(cityService.updateCity(eq(cityId), any(City.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cities/{id}", cityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated City\",\"countryId\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCitySuccess() throws Exception {
    	
        when(cityService.deleteCity(cityId)).thenReturn(true);

        mockMvc.perform(delete("/api/cities/{id}", cityId))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCityNotFound() throws Exception {
    	
        when(cityService.deleteCity(cityId)).thenReturn(false);

        mockMvc.perform(delete("/api/cities/{id}", cityId))
                .andExpect(status().isNotFound());
    }
}