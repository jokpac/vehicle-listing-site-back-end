package lt.ca.javau11.services;

import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.exceptions.CountryNotFoundException;
import lt.ca.javau11.repositories.CityRepository;
import lt.ca.javau11.repositories.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    private City city;
    private Long cityId = 1L;
    private Long countryId = 2L;
    private Country country;

    @BeforeEach
    public void setUp() {
    	
        country = new Country();
        country.setId(countryId);
        country.setName("Test Country");

        city = new City();
        city.setId(cityId);
        city.setName("Test City");
        city.setCountry(country);
    }

    @Test
    public void testGetAllCities() {
    	
        when(cityRepository.findAll()).thenReturn(List.of(city));

        List<City> cities = cityService.getAll();

        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertEquals("Test City", cities.get(0).getName());
    }

    @Test
    public void testAddCitySuccess() {
    	
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        when(cityRepository.save(city)).thenReturn(city);

        City createdCity = cityService.addCity(countryId, city);

        assertNotNull(createdCity);
        assertEquals("Test City", createdCity.getName());
        assertEquals("Test Country", createdCity.getCountry().getName());
    }

    @Test
    public void testAddCityCountryNotFound() {

    	when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> {
            cityService.addCity(countryId, city);
        });
    }

    @Test
    public void testGetCityById() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        Optional<City> result = cityService.getById(cityId);

        assertTrue(result.isPresent());
        assertEquals("Test City", result.get().getName());
    }

    @Test
    public void testGetCityByIdNotFound() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        Optional<City> result = cityService.getById(cityId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateCitySuccess() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        City updatedCity = new City();
        updatedCity.setId(cityId);
        updatedCity.setName("Updated City");
        updatedCity.setCountry(country);

        when(cityRepository.save(any(City.class))).thenReturn(updatedCity);

        Optional<City> result = cityService.updateCity(cityId, updatedCity);

        assertTrue(result.isPresent());
        assertEquals("Updated City", result.get().getName());
    }

    @Test
    public void testUpdateCityNotFound() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        Optional<City> result = cityService.updateCity(cityId, city);

        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteCitySuccess() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        doNothing().when(cityRepository).delete(city);

        boolean isDeleted = cityService.deleteCity(cityId);

        assertTrue(isDeleted);
        verify(cityRepository, times(1)).delete(city);
    }

    @Test
    public void testDeleteCityNotFound() {

    	when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        boolean isDeleted = cityService.deleteCity(cityId);

        assertFalse(isDeleted);
    }
}
