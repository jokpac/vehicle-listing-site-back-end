package lt.ca.javau11.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lt.ca.javau11.entities.*;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.exceptions.*;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.repositories.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ListingService listingService;

    private ListingDTO listingDTO;
    private Listing listing;
    private User user;
    private Country country;
    private City city;
    private Make make;
    private Model model;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", "test@example.com");
        user.setId(1L);

        country = new Country();
        country.setId(1L);
        country.setName("Lithuania");

        city = new City();
        city.setId(1L);
        city.setName("Vilnius");

        make = new Make();
        make.setId(1L);
        make.setName("Toyota");

        model = new Model();
        model.setId(1L);
        model.setName("Corolla");

        listing = new Listing();
        listing.setId(1L);
        listing.setUser(user);
        listing.setCountry(country);
        listing.setCity(city);
        listing.setMake(make);
        listing.setModel(model);
        listing.setTitle("Test Listing");

        listingDTO = new ListingDTO();
        listingDTO.setTitle("Updated Listing");
        listingDTO.setCountryId(1L);
        listingDTO.setCityId(1L);
        listingDTO.setMakeId(1L);
        listingDTO.setModelId(1L);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void getAllListings_ShouldReturnListOfListings() {
        when(listingRepository.findAll()).thenReturn(List.of(listing));

        List<Listing> listings = listingService.getAllListings();

        assertNotNull(listings);
        assertEquals(1, listings.size());
    }

    @Test
    void getListingById_ShouldReturnListing() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        Optional<Listing> result = listingService.getListingById(1L);

        assertTrue(result.isPresent());
        assertEquals(listing.getId(), result.get().getId());
    }

    @Test
    void getUserListings_ShouldReturnUserListings() {
        when(listingRepository.findListingsByUserId(1L)).thenReturn(List.of(listing));

        List<Listing> listings = listingService.getUserListings(1L);

        assertEquals(1, listings.size());
        assertEquals("Test Listing", listings.get(0).getTitle());
    }

    @Test
    void createListing_ShouldReturnCreatedListing() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(makeRepository.findById(1L)).thenReturn(Optional.of(make));
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        Listing createdListing = listingService.createListing(listingDTO);

        assertNotNull(createdListing);
        assertEquals("Test Listing", createdListing.getTitle());
    }

    @Test
    void createListing_ShouldThrowException_WhenCountryNotFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> listingService.createListing(listingDTO));
    }

    @Test
    void updateListing_ShouldReturnUpdatedListing() {
        when(authentication.getName()).thenReturn("testuser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(makeRepository.findById(1L)).thenReturn(Optional.of(make));
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        Optional<Listing> updatedListing = listingService.updateListing(1L, listingDTO);

        assertTrue(updatedListing.isPresent());
        assertEquals("Updated Listing", updatedListing.get().getTitle());
    }

    @Test
    void updateListing_ShouldThrowException_WhenNotOwner() {
        when(authentication.getName()).thenReturn("otheruser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        assertThrows(RuntimeException.class, () -> listingService.updateListing(1L, listingDTO));
    }

    @Test
    void updateListingStatus_ShouldReturnUpdatedListing() {
        when(authentication.getName()).thenReturn("testuser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        Optional<Listing> updatedListing = listingService.updateListingStatus(1L, ListingStatus.ACTIVE);

        assertTrue(updatedListing.isPresent());
        assertEquals(ListingStatus.ACTIVE, updatedListing.get().getListingStatus());
    }

    @Test
    void updateListingStatus_ShouldThrowException_WhenNotOwner() {
        when(authentication.getName()).thenReturn("otheruser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        assertThrows(RuntimeException.class, () -> listingService.updateListingStatus(1L, ListingStatus.ACTIVE));
    }

    @Test
    void deleteListing_ShouldReturnTrue_WhenListingExists() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        boolean result = listingService.deleteListing(1L);

        assertTrue(result);
        verify(listingRepository, times(1)).delete(listing);
    }

    @Test
    void deleteListing_ShouldReturnFalse_WhenListingNotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = listingService.deleteListing(1L);

        assertFalse(result);
    }

    @Test
    void isListingOwner_ShouldReturnTrue_WhenUserIsOwner() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        boolean result = listingService.isListingOwner(1L, "testuser");

        assertTrue(result);
    }

    @Test
    void isListingOwner_ShouldReturnFalse_WhenUserIsNotOwner() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        boolean result = listingService.isListingOwner(1L, "otheruser");

        assertFalse(result);
    }

    @Test
    void isListingOwner_ShouldReturnFalse_WhenListingNotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = listingService.isListingOwner(1L, "testuser");

        assertFalse(result);
    }
}
