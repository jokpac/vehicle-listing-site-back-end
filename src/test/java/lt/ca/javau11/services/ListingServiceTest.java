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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lt.ca.javau11.entities.*;
import lt.ca.javau11.enums.DrivenWheels;
import lt.ca.javau11.enums.FuelType;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.enums.ListingType;
import lt.ca.javau11.enums.Transmission;
import lt.ca.javau11.exceptions.*;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.repositories.*;
import lt.ca.javau11.utils.EntityMapper;
import lt.ca.javau11.utils.ListingSpecification;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock private ListingRepository listingRepository;
    @Mock private CountryRepository countryRepository;
    @Mock private CityRepository cityRepository;
    @Mock private MakeRepository makeRepository;
    @Mock private ModelRepository modelRepository;
    @Mock private UserRepository userRepository;
    @Mock private EntityMapper entityMapper;

    @InjectMocks private ListingService listingService;

    private Listing listing;
    private ListingDTO listingDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        
        listing = new Listing();
        listing.setId(1L);
        listing.setUser(user);
        
        listingDTO = new ListingDTO();
        listingDTO.setId(1L);
        listingDTO.setTitle("Title");
        listingDTO.setYear(2020);
        listingDTO.setMonth(1);
        listingDTO.setMakeId(1L);
        listingDTO.setModelId(1L);
        listingDTO.setCountryId(1L);
        listingDTO.setCityId(1L);
        listingDTO.setDescription("Description");
        listingDTO.setMileage(200000);
        listingDTO.setEnginePower(100);
        listingDTO.setEngineSize(2.0);
        listingDTO.setPrice(5000.0);
        listingDTO.setImageURLs(List.of("image/1", "image/2"));
        listingDTO.setDrivenWheels(DrivenWheels.FWD);
        listingDTO.setTransmission(Transmission.AUTOMATIC);
        listingDTO.setFuelType(FuelType.DIESEL);
        listingDTO.setListingStatus(ListingStatus.ACTIVE);
        listingDTO.setListingType(ListingType.SALE);
    }

    @Test
    void testGetAllListings() {
        when(listingRepository.findAll()).thenReturn(List.of(listing));
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);
        
        List<ListingDTO> result = listingService.getAllListings();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(listingDTO, result.get(0));
    }

    @Test
    void testGetListingById() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);
        
        Optional<ListingDTO> result = listingService.getListingById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(listingDTO, result.get());
    }
    
    @Test
    void testGetUserListings() {
        when(listingRepository.findListingsByUserId(1L)).thenReturn(List.of(listing));
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);
        
        List<ListingDTO> result = listingService.getUserListings(1L);
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(listingDTO, result.get(0));
    }

    @Test
    void testCreateListing_Success() {
        mockSecurityContext("testUser");
        
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(entityMapper.toListing(any(), any(), any(), any(), any(), any())).thenReturn(listing);
        when(countryRepository.findById(any())).thenReturn(Optional.of(new Country()));
        when(cityRepository.findById(any())).thenReturn(Optional.of(new City()));
        when(makeRepository.findById(any())).thenReturn(Optional.of(new Make()));
        when(modelRepository.findById(any())).thenReturn(Optional.of(new Model()));
        when(listingRepository.save(any())).thenReturn(listing);
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);
        
        ListingDTO result = listingService.createListing(listingDTO);
        
        assertNotNull(result);
        assertEquals(listingDTO, result);
    }

    @Test
    void testCreateListing_UserNotFound() {
        mockSecurityContext("testUser");
        
        when(countryRepository.findById(any())).thenReturn(Optional.of(new Country()));
        when(cityRepository.findById(any())).thenReturn(Optional.of(new City()));
        when(makeRepository.findById(any())).thenReturn(Optional.of(new Make()));
        when(modelRepository.findById(any())).thenReturn(Optional.of(new Model()));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> listingService.createListing(listingDTO));
    }

    @Test
    void testUpdateListing_Success() {
        mockSecurityContext("testUser");
        
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any())).thenReturn(listing);
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);
        
        Optional<ListingDTO> result = listingService.updateListing(1L, listingDTO);
        
        assertTrue(result.isPresent());
        assertEquals(listingDTO, result.get());
    }

    @Test
    void testUpdateListing_Unauthorized() {
        mockSecurityContext("anotherUser");
        
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        
        assertThrows(RuntimeException.class, () -> listingService.updateListing(1L, listingDTO));
    }

    @Test
    void testDeleteListing_Success() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        doNothing().when(listingRepository).delete(listing);
        
        assertTrue(listingService.deleteListing(1L));
    }

    @Test
    void testDeleteListing_NotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertFalse(listingService.deleteListing(1L));
    }
    
    @Test
    void testUpdateListingStatus_Success() {
        mockSecurityContext("testUser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any())).thenReturn(listing);
        when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);

        Optional<ListingDTO> result = listingService.updateListingStatus(1L, ListingStatus.ACTIVE);
        
        assertTrue(result.isPresent());
        assertEquals(listingDTO, result.get());
    }

    @Test
    void testUpdateListingStatus_Unauthorized() {
        mockSecurityContext("anotherUser");
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        
        assertThrows(RuntimeException.class, () -> listingService.updateListingStatus(1L, ListingStatus.ACTIVE));
    }

    @Test
    void testIsListingOwner_True() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        
        assertTrue(listingService.isListingOwner(1L, "testUser"));
    }

    @Test
    void testIsListingOwner_False() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        
        assertFalse(listingService.isListingOwner(1L, "anotherUser"));
    }

    @Test
    void testIsListingOwner_NotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertFalse(listingService.isListingOwner(1L, "testUser"));
    }

    @SuppressWarnings("unchecked")
	@Test
    void testGetFilteredListings() {
        Specification<Listing> spec = mock(Specification.class);
        try (MockedStatic<ListingSpecification> mockedSpec = mockStatic(ListingSpecification.class)) {
            mockedSpec.when(() -> ListingSpecification.filterListings(
                    any(), any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(spec);
            when(listingRepository.findAll(any(Specification.class))).thenReturn(List.of(listing));
            when(entityMapper.toListingDto(listing)).thenReturn(listingDTO);

            List<ListingDTO> result = listingService.getFilteredListings(
                    null, null, null, null, null, null, null, null, null, null, 
                    null, null, null, null, null, null, null);

            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals(listingDTO, result.get(0));
        }
    }

    private void mockSecurityContext(String username) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}
