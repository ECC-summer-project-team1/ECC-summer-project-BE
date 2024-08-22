package com.toilet_search.demo.toilet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toilet_search.demo.toilet.model.Toilet;
import com.toilet_search.demo.toilet.model.dto.ToiletSearchCriteriaDto;
import com.toilet_search.demo.toilet.service.ToiletService;

@RestController
@RequestMapping("/api/toilets")
public class ToiletController {

	@Autowired
	private ToiletService toiletService;

	@PostMapping("/upload")
	public String uploadToiletData(@RequestParam("file") MultipartFile file) {
		toiletService.saveToiletsFromExcel(file);
		return "파일 데이터가 성공적으로 저장되었습니다! ";
	}

	@GetMapping("/update-coordinates")
	public String updateCoordinates(@RequestParam("apiKey") String apiKey) {
		toiletService.updateToiletsWithCoordinates(apiKey);
		return "위도와 경도 값이 성공적으로 업데이트되었습니다! ";
	}

	// 특정 주소에 있는 화장실 데이터를 검색하여 가져옴
	@GetMapping("/search/address")
	public List<Toilet> searchToiletsByAddress(@RequestParam("address") String address) {
		return toiletService.searchToiletsByAddress(address);
	}

	// 위치 기반 검색
	@GetMapping("/search/location")
	public List<Toilet> searchToiletsByLocation(
		@RequestParam("lat") double userLat,
		@RequestParam("lon") double userLon,
		@RequestParam("radius") double radius) {
		return toiletService.searchToiletsByLocation(userLat, userLon, radius);
	}

	@PostMapping("/search")
	public List<Toilet> searchToilets(
		@RequestBody ToiletSearchCriteriaDto searchCriteriaDto
	) {
		return toiletService.searchToilets(searchCriteriaDto);
	}
}
