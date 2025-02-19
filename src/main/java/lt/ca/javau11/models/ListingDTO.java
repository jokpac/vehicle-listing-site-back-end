package lt.ca.javau11.models;


import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lt.ca.javau11.enums.DrivenWheels;
import lt.ca.javau11.enums.FuelType;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.enums.ListingType;
import lt.ca.javau11.enums.Transmission;
import lt.ca.javau11.utils.ValidYear;

public class ListingDTO {

	private Long id;
	
    @NotNull(message = "Country is required")
    private Long countryId;

    @NotNull(message = "City is required")
    private Long cityId;

    @NotNull(message = "Make is required")
    private Long makeId;

    @NotNull(message = "Model is required")
    private Long modelId;

    private String countryName;
    private String cityName;
    private String makeName;
    private String modelName;

    private Long userId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Year is required")
    @ValidYear
    private Integer year;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Mileage is required")
    @Min(value = 0, message = "Mileage must be greater than or equal to 0")
    private Integer mileage;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @NotNull(message = "Engine size is required")
    @Positive(message = "Engine size must be greater than 0")
    private Double engineSize;

    @NotNull(message = "Engine power is required")
    @Positive(message = "Engine power must be greater than 0")
    private Integer enginePower;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotNull(message = "Transmission type is required")
    private Transmission transmission;

    @NotNull(message = "Driven wheels selection is required")
    private DrivenWheels drivenWheels;

    @NotNull(message = "Listing type is required")
    private ListingType listingType;

    private ListingStatus listingStatus;

    private List<String> imageURLs;
    
    public ListingDTO() {}

	public ListingDTO(Long id, Long countryId, Long cityId, Long makeId, Long modelId, String countryName,
			String cityName, String makeName, String modelName, Long userId, String title, Double price, int year,
			int month, int mileage, String description, double engineSize, int enginePower, FuelType fuelType,
			Transmission transmission, DrivenWheels drivenWheels, ListingType listingType, ListingStatus listingStatus,
			List<String> imageURLs) {
		this.id = id;
		this.countryId = countryId;
		this.cityId = cityId;
		this.makeId = makeId;
		this.modelId = modelId;
		this.countryName = countryName;
		this.cityName = cityName;
		this.makeName = makeName;
		this.modelName = modelName;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.year = year;
		this.month = month;
		this.mileage = mileage;
		this.description = description;
		this.engineSize = engineSize;
		this.enginePower = enginePower;
		this.fuelType = fuelType;
		this.transmission = transmission;
		this.drivenWheels = drivenWheels;
		this.listingType = listingType;
		this.listingStatus = listingStatus;
		this.imageURLs = imageURLs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getMakeId() {
		return makeId;
	}

	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getEngineSize() {
		return engineSize;
	}

	public void setEngineSize(double engineSize) {
		this.engineSize = engineSize;
	}

	public int getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(int enginePower) {
		this.enginePower = enginePower;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public Transmission getTransmission() {
		return transmission;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	public DrivenWheels getDrivenWheels() {
		return drivenWheels;
	}

	public void setDrivenWheels(DrivenWheels drivenWheels) {
		this.drivenWheels = drivenWheels;
	}

	public ListingType getListingType() {
		return listingType;
	}

	public void setListingType(ListingType listingType) {
		this.listingType = listingType;
	}

	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(ListingStatus listingStatus) {
		this.listingStatus = listingStatus;
	}

	public List<String> getImageURLs() {
		return imageURLs;
	}

	public void setImageURLs(List<String> imageURLs) {
		this.imageURLs = imageURLs;
	}

}