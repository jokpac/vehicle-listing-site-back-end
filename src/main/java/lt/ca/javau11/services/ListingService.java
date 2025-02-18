package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.entities.*;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.exceptions.*;
import lt.ca.javau11.repositories.*;
import lt.ca.javau11.utils.EntityMapper;
import lt.ca.javau11.utils.ListingSpecification;

@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final MakeRepository makeRepository;
    private final ModelRepository modelRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    public ListingService(ListingRepository listingRepository,
                          CountryRepository countryRepository,
                          CityRepository cityRepository,
                          MakeRepository makeRepository,
                          ModelRepository modelRepository,
                          UserRepository userRepository,
                          EntityMapper entityMapper) {
        this.listingRepository = listingRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.makeRepository = makeRepository;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    public List<ListingDTO> getAllListings() {
        return listingRepository.findAll().stream()
                .map(entityMapper::toListingDto)
                .collect(Collectors.toList());
    }

    public Optional<ListingDTO> getListingById(Long id) {
        return listingRepository.findById(id).map(entityMapper::toListingDto);
    }

    public List<ListingDTO> getUserListings(Long userId) {
        return listingRepository.findListingsByUserId(userId).stream()
                .map(entityMapper::toListingDto)
                .collect(Collectors.toList());
    }

    public ListingDTO createListing(ListingDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));
        Make make = makeRepository.findById(request.getMakeId())
                .orElseThrow(() -> new MakeNotFoundException("Make not found"));
        Model model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> new ModelNotFoundException("Model not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Listing listing = entityMapper.toListing(request, country, city, make, model, user);

        return entityMapper.toListingDto(listingRepository.save(listing));
    }

    public Optional<ListingDTO> updateListing(Long id, ListingDTO updatedListing) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return listingRepository.findById(id).map(existingListing -> {
            if (!existingListing.getUser().getUsername().equals(username)) {
                throw new RuntimeException("You are not authorized to update this listing");
            }

            existingListing.setTitle(updatedListing.getTitle());
            existingListing.setPrice(updatedListing.getPrice());
            existingListing.setYear(updatedListing.getYear());
            existingListing.setMonth(updatedListing.getMonth());
            existingListing.setMileage(updatedListing.getMileage());
            existingListing.setDescription(updatedListing.getDescription());
            existingListing.setEngineSize(updatedListing.getEngineSize());
            existingListing.setEnginePower(updatedListing.getEnginePower());
            existingListing.setFuelType(updatedListing.getFuelType());
            existingListing.setTransmission(updatedListing.getTransmission());
            existingListing.setDrivenWheels(updatedListing.getDrivenWheels());
            existingListing.setListingType(updatedListing.getListingType());
            existingListing.setImageURLs(updatedListing.getImageURLs());

            return entityMapper.toListingDto(listingRepository.save(existingListing));
        });
    }

    public Optional<ListingDTO> updateListingStatus(Long id, ListingStatus status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return listingRepository.findById(id).map(existingListing -> {
            if (!existingListing.getUser().getUsername().equals(username)) {
                throw new RuntimeException("You are not authorized to update this listing");
            }

            existingListing.setListingStatus(status);
            return entityMapper.toListingDto(listingRepository.save(existingListing));
        });
    }

    public boolean deleteListing(Long id) {
        return listingRepository.findById(id).map(listing -> {
            listingRepository.delete(listing);
            return true;
        }).orElse(false);
    }
    
    public boolean isListingOwner(Long listingId, String username) {
        return listingRepository.findById(listingId)
            .map(listing -> listing.getUser().getUsername().equals(username))
            .orElse(false);
    }

    public List<ListingDTO> getFilteredListings(
            Double priceMin, Double priceMax,
            Integer yearMin, Integer yearMax,
            Integer mileageMin, Integer mileageMax,
            Double engineSizeMin, Double engineSizeMax,
            String fuelType, String transmission, String drivenWheels,
            Long countryId, Long cityId, Long makeId, Long modelId, String listingStatus,
            String listingType) {

        Specification<Listing> spec = ListingSpecification.filterListings(
                priceMin, priceMax, yearMin, yearMax,
                mileageMin, mileageMax, engineSizeMin, engineSizeMax,
                fuelType, transmission, drivenWheels,
                countryId, cityId, makeId, modelId, listingStatus, listingType);

        List<Listing> results = listingRepository.findAll(spec);

        return results.stream()
                .map(entityMapper::toListingDto)
                .collect(Collectors.toList());
    }
}
