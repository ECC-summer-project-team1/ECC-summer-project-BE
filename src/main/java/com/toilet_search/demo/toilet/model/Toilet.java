package com.toilet_search.demo.toilet.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Toilet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본 키

	private String category; // 구분
	private String reference; // 근거
	private String toiletName; // 화장실명
	private String addressRoad; // 도로명 주소
	private String addressJibun; // 지번 주소
	private int maleToiletCount; // 남성용 대변기 수
	private int maleUrinalCount; // 남성용 소변기 수
	private int maleDisabledToiletCount; // 남성용 장애인 대변기 수
	private int maleDisabledUrinalCount; // 남성용 장애인 소변기 수
	private int maleChildToiletCount; // 남성용 어린이 대변기 수
	private int maleChildUrinalCount; // 남성용 어린이 소변기 수
	private int femaleToiletCount; // 여성용 대변기 수
	private int femaleDisabledToiletCount; // 여성용 장애인 대변기 수
	private int femaleChildToiletCount; // 여성용 어린이 대변기 수
	private String managingInstitution; // 관리기관명
	private String phoneNumber; // 전화번호
	private String openingHours; // 개방시간
	private String detailedOpeningHours; // 개방시간 상세
	private String installationDate; // 설치 연월
	private double latitude; // 위도
	private double longitude; // 경도
	private String ownershipType; // 소유구분
	private String wasteManagementType; // 오물처리방식
	private String safetyFacilityRequired; // 안전관리시설 설치 여부
	private String emergencyBell; // 비상벨 설치 여부
	private String emergencyBellLocation; // 비상벨 설치 장소
	private String cctv; // 화장실 입구 CCTV 설치 유무
	private String diaperChangingStation; // 기저귀 교환대 유무
	private String diaperChangingStationLocation; // 기저귀 교환대 설치 장소
	private String remodelingDate; // 리모델링 연월
	private String dataStandardDate; // 데이터 기준일자


}