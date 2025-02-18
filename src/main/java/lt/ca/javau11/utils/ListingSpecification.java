package lt.ca.javau11.utils;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import lt.ca.javau11.entities.Listing;

import java.util.ArrayList;
import java.util.List;

public class ListingSpecification {

	public static Specification<Listing> filterListings(Double priceMin, Double priceMax, Integer yearMin,
			Integer yearMax, Integer mileageMin, Integer mileageMax, Double engineSizeMin, Double engineSizeMax,
			String fuelType, String transmission, String drivenWheels, Long countryId, Long cityId, Long makeId,
			Long modelId, String listingStatus, String listingType) {

		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (priceMin != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
			}
			if (priceMax != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
			}
			if (yearMin != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), yearMin));
			}
			if (yearMax != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("year"), yearMax));
			}
			if (mileageMin != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("mileage"), mileageMin));
			}
			if (mileageMax != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("mileage"), mileageMax));
			}
			if (engineSizeMin != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("engineSize"), engineSizeMin));
			}
			if (engineSizeMax != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("engineSize"), engineSizeMax));
			}
			if (fuelType != null && !fuelType.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("fuelType"), fuelType));
			}
			if (transmission != null && !transmission.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("transmission"), transmission));
			}
			if (drivenWheels != null && !drivenWheels.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("drivenWheels"), drivenWheels));
			}
			if (countryId != null) {
				predicates.add(criteriaBuilder.equal(root.get("country").get("id"), countryId));
			}
			if (cityId != null) {
				predicates.add(criteriaBuilder.equal(root.get("city").get("id"), cityId));
			}
			if (makeId != null) {
				predicates.add(criteriaBuilder.equal(root.get("make").get("id"), makeId));
			}
			if (modelId != null) {
				predicates.add(criteriaBuilder.equal(root.get("model").get("id"), modelId));
			}
			if (listingStatus != null) {
			    predicates.add(criteriaBuilder.equal(root.get("listingStatus"), listingStatus));
			}
			if (listingType != null && !listingType.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("listingType"), listingType));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
