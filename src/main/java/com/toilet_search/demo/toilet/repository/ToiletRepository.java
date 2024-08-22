package com.toilet_search.demo.toilet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.toilet_search.demo.toilet.model.Toilet;

public interface ToiletRepository extends JpaRepository<Toilet, Long> {
	List<Toilet> findByLatitudeAndLongitude(double latitude, double longitude);
	List<Toilet> findByAddressRoadContainingOrAddressJibunContaining(String roadAddress, String jibunAddress);

}
