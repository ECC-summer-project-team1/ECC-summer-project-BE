package com.toilet_search.demo.toilet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toilet_search.demo.toilet.model.Toilet;
import com.toilet_search.demo.toilet.service.ToiletService;

@RestController
@RequestMapping("/api/toilets")
public class ToiletController {

	@Autowired
	private ToiletService toiletService;

	// 특정 주소에 있는 화장실 데이터를 검색하여 가져옴
	@GetMapping("/search/address")
	public List<Toilet> searchToiletsByAddress(
		@RequestParam("file") MultipartFile file,
		@RequestParam("address") String address
	) {
		return toiletService.searchToiletsByAddress(file, address);
	}

	// 위치 기반 검색
	@GetMapping("/search/location")
	public List<Toilet> searchToiletsByLocation(
		@RequestParam("file") MultipartFile file,
		@RequestParam("lat") double userLat,
		@RequestParam("lon") double userLon,
		@RequestParam("radius") double radius) {
		return toiletService.searchToiletsByLocation(file, userLat, userLon, radius);
	}


	//test용
	@GetMapping("/wow")
	public String getWow() {
		return "wow";
	}
}
