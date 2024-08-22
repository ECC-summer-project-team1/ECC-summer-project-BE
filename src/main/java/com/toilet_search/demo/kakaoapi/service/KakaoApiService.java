package com.toilet_search.demo.kakaoapi.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KakaoApiService {

	private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";

	public double[] getCoordinates(String address, String apiKey) {
		RestTemplate restTemplate = new RestTemplate();

		// Query 인코딩
		String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

		String uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
			.queryParam("query", address)
			.build()
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + apiKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoApiResponse.class);
		log.info(response.getBody().toString());

		if (response.getStatusCode().is2xxSuccessful()) {
			KakaoApiResponse body = response.getBody();
			if (body != null && !body.getDocuments().isEmpty()) {
				KakaoApiResponse.Document document = body.getDocuments().get(0);
				double x = Double.parseDouble(document.getX());
				double y = Double.parseDouble(document.getY());
				return new double[]{y, x}; // [위도, 경도]
			}
		}
		return null; // 좌표를 찾지 못한 경우
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true) // JSON 응답에 매핑되지 않는 필드를 무시
	public static class KakaoApiResponse {
		private List<Document> documents;

		@Data
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Document {
			private String x; // 경도
			private String y; // 위도
			private String address_name; // 전체 주소
			private String address_type; // 주소 유형
			private Address address; // 지번 주소 상세 정보
			private RoadAddress road_address; // 도로명 주소 상세 정보
		}

		@Data
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Address {
			private String address_name;
			private String b_code;
			private String h_code;
			private String main_address_no;
			private String mountain_yn;
			private String region_1depth_name;
			private String region_2depth_name;
			private String region_3depth_h_name;
			private String region_3depth_name;
			private String sub_address_no;
			private String x;
			private String y;
		}

		@Data
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class RoadAddress {
			private String address_name;
			private String building_name;
			private String main_building_no;
			private String region_1depth_name;
			private String region_2depth_name;
			private String region_3depth_name;
			private String road_name;
			private String sub_building_no;
			private String underground_yn;
			private String x;
			private String y;
			private String zone_no;
		}
	}
}
