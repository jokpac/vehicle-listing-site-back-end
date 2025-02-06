package lt.ca.javau11.services;

import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.models.CityDTO;
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
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country;
    private Long countryId = 1L;

    @BeforeEach
    public void setUp() {
        country = new Country();
        country.setId(countryId);
        country.setName("Test Country");

        // Create some cities for testing getCitiesByCountry
        City city1 = new City();
        city1.setId(1L);
        city1.setName("City 1");
        City city2 = new City();
        city2.setId(2L);
        city2.setName("City 2");
        country.setCities(List.of(city1, city2));
    }

    @Test
    public void testGetAllCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(country));

        List<Country> countries = countryService.getAll();

        assertNotNull(countries);
        assertEquals(1, countries.size());
        assertEquals("Test Country", countries.get(0).getName());
    }

    @Test
    public void testAddCountry() {
        when(countryRepository.save(country)).thenReturn(country);

        Country createdCountry = countryService.addCountry(country);

        assertNotNull(createdCountry);
        assertEquals("Test Country", createdCountry.getName());
    }

    @Test
    public void testGetCountryById_Found() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        Optional<Country> foundCountry = countryService.getById(countryId);

        assertTrue(foundCountry.isPresent());
        assertEquals("Test Country", foundCountry.get().getName());
    }

    @Test
    public void testGetCountryById_NotFound() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        Optional<Country> foundCountry = countryService.getById(countryId);

        assertFalse(foundCountry.isPresent());
    }

    @Test
    public void testGetCitiesByCountry_Found() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        Optional<List<CityDTO>> cities = countryService.getCitiesByCountry(countryId);

        assertTrue(cities.isPresent());
        assertEquals(2, cities.get().size());
        assertEquals("City 1", cities.get().get(0).getName());
        assertEquals("City 2", cities.get().get(1).getName());
    }

    @Test
    public void testGetCitiesByCountry_NotFound() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        Optional<List<CityDTO>> cities = countryService.getCitiesByCountry(countryId);

        assertFalse(cities.isPresent());
    }

    @Test
    public void testUpdateCountry_Success() {
        Country updatedCountryDetails = new Country();
        updatedCountryDetails.setName("Updated Country");

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);

        Optional<Country> updatedCountry = countryService.updateCountry(countryId, updatedCountryDetails);

        assertTrue(updatedCountry.isPresent());
        assertEquals("Updated Country", updatedCountry.get().getName());
    }

    @Test
    public void testUpdateCountry_NotFound() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        Optional<Country> updatedCountry = countryService.updateCountry(countryId, country);

        assertFalse(updatedCountry.isPresent());
    }

    @Test
    public void testDeleteCountry_Success() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        boolean isDeleted = countryService.deleteCountry(countryId);

        assertTrue(isDeleted);
        verify(countryRepository, times(1)).delete(country);
    }

    @Test
    public void testDeleteCountry_NotFound() {
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        boolean isDeleted = countryService.deleteCountry(countryId);

        assertFalse(isDeleted);
    }
}
