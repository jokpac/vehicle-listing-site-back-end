package lt.ca.javau11.models;


import java.util.ArrayList;
import java.util.List;

import lt.ca.javau11.enums.DrivenWheels;
import lt.ca.javau11.enums.FuelType;
import lt.ca.javau11.enums.ListingStatus;
import lt.ca.javau11.enums.ListingType;
import lt.ca.javau11.enums.Transmission;

public class ListingDTO {

    private Long countryId;
    private Long cityId;
    private Long makeId;
    private Long modelId;
    private Long userId;
    private String title;
    private Double price;
    private int year;
    private int month;
    private int mileage;
    private String description;
    private double engineSize;
    private int enginePower;
    private FuelType fuelType;
    private Transmission transmission;
    private DrivenWheels drivenWheels;
    private ListingType listingType;
    private ListingStatus listingStatus;
    private List<String> imageURLs = new ArrayList<>();
    
    public ListingDTO() {}

	public ListingDTO(Long countryId, Long cityId, Long makeId, Long modelId, Long userId, String title, Double price,
			int year, int month, int mileage, String description, double engineSize, int enginePower, FuelType fuelType,
			Transmission transmission, DrivenWheels drivenWheels, ListingType listingType, ListingStatus listingStatus,
			List<String> imageURLs) {
		this.countryId = countryId;
		this.cityId = cityId;
		this.makeId = makeId;
		this.modelId = modelId;
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

	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(ListingStatus listingStatus) {
		this.listingStatus = listingStatus;
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

	public List<String> getImageURLs() {
		return imageURLs;
	}

	public void setImageURLs(List<String> imageURLs) {
		this.imageURLs = imageURLs;
	}

    
}