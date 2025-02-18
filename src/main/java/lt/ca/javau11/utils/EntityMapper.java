package lt.ca.javau11.utils;

import org.springframework.stereotype.Component;

import lt.ca.javau11.entities.City;
import lt.ca.javau11.entities.Country;
import lt.ca.javau11.entities.Listing;
import lt.ca.javau11.entities.Make;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.entities.User;
import lt.ca.javau11.models.ListingDTO;
import lt.ca.javau11.models.UserDTO;

@Component
public class EntityMapper {
	
	public User toUser(UserDTO dto) {
		
		User user = new User();
		user.setId(dto.getId());
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());	
		return user;		
	}
	
	public UserDTO toUserDto(User user) {
		
		return new UserDTO( 
				user.getId(), 
				user.getUsername(), 
				user.getEmail(), 
				user.getPassword(), 
				user.getRoles() 
			);
	}
	
	public ListingDTO toListingDto(Listing listing) {
	    return new ListingDTO(
	        listing.getId(),
	        listing.getCountry().getId(),
	        listing.getCity().getId(),
	        listing.getMake().getId(),
	        listing.getModel().getId(),
	        listing.getCountry().getName(),
	        listing.getCity().getName(),
	        listing.getMake().getName(),
	        listing.getModel().getName(),
	        listing.getUser().getId(),
	        listing.getTitle(),
	        listing.getPrice(),
	        listing.getYear(),
	        listing.getMonth(),
	        listing.getMileage(),
	        listing.getDescription(),
	        listing.getEngineSize(),
	        listing.getEnginePower(),
	        listing.getFuelType(),
	        listing.getTransmission(),
	        listing.getDrivenWheels(),
	        listing.getListingType(),
	        listing.getListingStatus(),
	        listing.getImageURLs()
	    );
	}

    public Listing toListing(ListingDTO dto, Country country, City city, Make make, Model model, User user) {
        Listing listing = new Listing();
        listing.setId(dto.getId());
        listing.setCountry(country);
        listing.setCity(city);
        listing.setMake(make);
        listing.setModel(model);
        listing.setUser(user);
        listing.setTitle(dto.getTitle());
        listing.setPrice(dto.getPrice());
        listing.setYear(dto.getYear());
        listing.setMonth(dto.getMonth());
        listing.setMileage(dto.getMileage());
        listing.setDescription(dto.getDescription());
        listing.setEngineSize(dto.getEngineSize());
        listing.setEnginePower(dto.getEnginePower());
        listing.setFuelType(dto.getFuelType());
        listing.setTransmission(dto.getTransmission());
        listing.setDrivenWheels(dto.getDrivenWheels());
        listing.setListingStatus(dto.getListingStatus());
        listing.setListingType(dto.getListingType());
        listing.setImageURLs(dto.getImageURLs());
        return listing;
    }
	
}