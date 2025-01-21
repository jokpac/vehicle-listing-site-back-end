package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.entities.Listing;
import lt.ca.javau11.entities.Make;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.entities.User;
import lt.ca.javau11.exceptions.CityNotFoundException;
import lt.ca.javau11.exceptions.CountryNotFoundException;
import lt.ca.javau11.exceptions.MakeNotFoundException;
import lt.ca.javau11.exceptions.ModelNotFoundException;
import lt.ca.javau11.exceptions.UserNotFoundException;
import lt.ca.javau11.repositories.CityRepository;
import lt.ca.javau11.repositories.CountryRepository;
import lt.ca.javau11.repositories.ListingRepository;
import lt.ca.javau11.repositories.MakeRepository;
import lt.ca.javau11.repositories.ModelRepository;
import lt.ca.javau11.repositories.UserRepository;

@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final MakeRepository makeRepository;
    private final ModelRepository modelRepository;
    private final UserRepository userRepository;
    
    public ListingService(
    						ListingRepository listingRepository, 
    						CountryRepository countryRepository, 
    						CityRepository cityRepository, 
            				MakeRepository makeRepository, 
            				ModelRepository modelRepository,
            				UserRepository userRepository
            				) {
    	
        this.listingRepository = listingRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.makeRepository = makeRepository;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
    }
    

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    public Listing createListing(ListingDTO request) {
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));
        Make make = makeRepository.findById(request.getMakeId())
                .orElseThrow(() -> new MakeNotFoundException("Make not found"));
        Model model = modelRepository.findById(request.getModelId())
                .orElseThrow(() -> new ModelNotFoundException("Model not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Listing listing = new Listing();
        listing.setCountry(country);
        listing.setCity(city);
        listing.setMake(make);
        listing.setModel(model);
        listing.setUser(user);
        listing.setTitle(request.getTitle());
        listing.setPrice(request.getPrice());
        listing.setYear(request.getYear());
        listing.setMonth(request.getMonth());
        listing.setMileage(request.getMileage());
        listing.setDescription(request.getDescription());
        listing.setEngineSize(request.getEngineSize());
        listing.setEnginePower(request.getEnginePower());
        listing.setFuelType(request.getFuelType());
        listing.setTransmission(request.getTransmission());
        listing.setDrivenWheels(request.getDrivenWheels());
        listing.setListingType(request.getListingType());
        listing.setImageURLs(request.getImageURLs());

        return listingRepository.save(listing);
    }

    public Optional<Listing> updateListing(Long id, Listing updatedListing, Long countryId, Long cityId, Long makeId, Long modelId) {
    	
        return listingRepository.findById(id).map(existingListing -> {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new CountryNotFoundException("Country not found"));
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("City not found"));
            Make make = makeRepository.findById(makeId)
                    .orElseThrow(() -> new MakeNotFoundException("Make not found"));
            Model model = modelRepository.findById(modelId)
                    .orElseThrow(() -> new ModelNotFoundException("Model not found"));

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
            existingListing.setListingStatus(updatedListing.getListingStatus());
            existingListing.setCountry(country);
            existingListing.setCity(city);
            existingListing.setMake(make);
            existingListing.setModel(model);

            return listingRepository.save(existingListing);
        });
    }

    public boolean deleteListing(Long id) {
        Optional<Listing> box = listingRepository.findById(id);
        if (box.isPresent()) {
            listingRepository.delete(box.get());
            return true;
        }
        return false;
    }
}
