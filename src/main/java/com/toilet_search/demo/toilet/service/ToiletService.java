package com.toilet_search.demo.toilet.service;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.toilet_search.demo.kakaoapi.service.KakaoApiService;
import com.toilet_search.demo.toilet.model.Toilet;
import com.toilet_search.demo.toilet.model.dto.ToiletSearchCriteriaDto;
import com.toilet_search.demo.toilet.repository.ToiletRepository;

@Service
public class ToiletService {

	@Autowired
	private ToiletRepository toiletRepository;

	@Autowired
	private KakaoApiService kakaoApiService;

	public void saveToiletsFromExcel(MultipartFile file) {
		try {
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			List<Toilet> toilets = new ArrayList<>();

			for (int i = 1; i < 5628; i++) {
				Row row = sheet.getRow(i);

				Toilet toilet = Toilet.builder()
					.category(getCellValueAsString(row.getCell(1)))
					.reference(getCellValueAsString(row.getCell(2)))
					.toiletName(getCellValueAsString(row.getCell(3)))
					.addressRoad(getCellValueAsString(row.getCell(4)))
					.addressJibun(getCellValueAsString(row.getCell(5)))
					.maleToiletCount(convertToInt(getCellValueAsString(row.getCell(6))))
					.maleUrinalCount(convertToInt(getCellValueAsString(row.getCell(7))))
					.maleDisabledToiletCount(convertToInt(getCellValueAsString(row.getCell(8))))
					.maleDisabledUrinalCount(convertToInt(getCellValueAsString(row.getCell(9))))
					.maleChildToiletCount(convertToInt(getCellValueAsString(row.getCell(10))))
					.maleChildUrinalCount(convertToInt(getCellValueAsString(row.getCell(11))))
					.femaleToiletCount(convertToInt(getCellValueAsString(row.getCell(12))))
					.femaleDisabledToiletCount(convertToInt(getCellValueAsString(row.getCell(13))))
					.femaleChildToiletCount(convertToInt(getCellValueAsString(row.getCell(14))))
					.managingInstitution(getCellValueAsString(row.getCell(15)))
					.phoneNumber(getCellValueAsString(row.getCell(16)))
					.openingHours(getCellValueAsString(row.getCell(17)))
					.detailedOpeningHours(getCellValueAsString(row.getCell(18)))
					.installationDate(getCellValueAsString(row.getCell(19)))
					.latitude(convertToDouble(getCellValueAsString(row.getCell(20))))
					.longitude(convertToDouble(getCellValueAsString(row.getCell(21))))
					.ownershipType(getCellValueAsString(row.getCell(22)))
					.wasteManagementType(getCellValueAsString(row.getCell(23)))
					.safetyFacilityRequired(getCellValueAsString(row.getCell(24)))
					.emergencyBell(getCellValueAsString(row.getCell(25)))
					.emergencyBellLocation(getCellValueAsString(row.getCell(26)))
					.cctv(getCellValueAsString(row.getCell(27)))
					.diaperChangingStation(getCellValueAsString(row.getCell(28)))
					.diaperChangingStationLocation(getCellValueAsString(row.getCell(29)))
					.remodelingDate(getCellValueAsString(row.getCell(30)))
					.dataStandardDate(getCellValueAsString(row.getCell(31)))
					.build();

				toilets.add(toilet);
			}

			toiletRepository.saveAll(toilets);
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("엑셀 파일을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
		}
	}


	@Transactional
	public void updateToiletsWithCoordinates(String apiKey) {
		// 위도와 경도가 0인 레코드를 모두 가져옵니다.
		List<Toilet> toiletsWithoutCoordinates = toiletRepository.findByLatitudeAndLongitude(0, 0);

		for (Toilet toilet : toiletsWithoutCoordinates) {
			String address = toilet.getAddressRoad();
			if (address == null || address.isEmpty()) {
				address = toilet.getAddressJibun();  // 주소가 비어 있으면 지번 주소를 사용
			}

			if (address != null && !address.isEmpty()) {
				double[] coordinates = kakaoApiService.getCoordinates(address, apiKey);
				if (coordinates != null) {
					Toilet updatedToilet = Toilet.builder()
						.id(toilet.getId())  // 기존 ID를 유지해야 함
						.category(toilet.getCategory())
						.reference(toilet.getReference())
						.toiletName(toilet.getToiletName())
						.addressRoad(toilet.getAddressRoad())
						.addressJibun(toilet.getAddressJibun())
						.maleToiletCount(toilet.getMaleToiletCount())
						.maleUrinalCount(toilet.getMaleUrinalCount())
						.maleDisabledToiletCount(toilet.getMaleDisabledToiletCount())
						.maleDisabledUrinalCount(toilet.getMaleDisabledUrinalCount())
						.maleChildToiletCount(toilet.getMaleChildToiletCount())
						.maleChildUrinalCount(toilet.getMaleChildUrinalCount())
						.femaleToiletCount(toilet.getFemaleToiletCount())
						.femaleDisabledToiletCount(toilet.getFemaleDisabledToiletCount())
						.femaleChildToiletCount(toilet.getFemaleChildToiletCount())
						.managingInstitution(toilet.getManagingInstitution())
						.phoneNumber(toilet.getPhoneNumber())
						.openingHours(toilet.getOpeningHours())
						.detailedOpeningHours(toilet.getDetailedOpeningHours())
						.installationDate(toilet.getInstallationDate())
						.latitude(coordinates[0])  // 새로운 위도 값 (y)
						.longitude(coordinates[1])  // 새로운 경도 값 (x)
						.ownershipType(toilet.getOwnershipType())
						.wasteManagementType(toilet.getWasteManagementType())
						.safetyFacilityRequired(toilet.getSafetyFacilityRequired())
						.emergencyBell(toilet.getEmergencyBell())
						.emergencyBellLocation(toilet.getEmergencyBellLocation())
						.cctv(toilet.getCctv())
						.diaperChangingStation(toilet.getDiaperChangingStation())
						.diaperChangingStationLocation(toilet.getDiaperChangingStationLocation())
						.remodelingDate(toilet.getRemodelingDate())
						.dataStandardDate(toilet.getDataStandardDate())
						.build();

					toiletRepository.save(updatedToilet);  // 업데이트된 정보를 DB에 저장
				}
			}
		}
	}

	private Integer convertToInt(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0;  // 기본값을 null로 설정
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;  // 기본값으로 null 반환
		}
	}

	private Double convertToDouble(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0.0;  // 기본값을 null로 설정
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0.0;  // 기본값으로 null 반환
		}
	}


	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}
		try {
			return cell.getStringCellValue().trim();
		} catch (Exception e) {
			return "";
		}
	}

	// 주소 기반 검색
	public List<Toilet> searchToiletsByAddress(String address) {
		return toiletRepository.findByAddressRoadContainingOrAddressJibunContaining(address, address);
	}

	// 위치 기반 검색
	public List<Toilet> searchToiletsByLocation(double userLat, double userLon, double radius) {
		List<Toilet> allToilets = toiletRepository.findAll();

		return allToilets.stream()
			.filter(toilet -> calculateDistance(userLat, userLon, toilet.getLatitude(), toilet.getLongitude()) <= radius)
			.collect(Collectors.toList());
	}

	// 거리 계산 메서드 (단위: 미터)
	private double calculateDistance(double userLat, double userLon, double toiletLat, double toiletLon) {
		double earthRadius = 6371000; // 미터 단위
		double dLat = Math.toRadians(toiletLat - userLat);
		double dLon = Math.toRadians(toiletLon - userLon);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(toiletLat)) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return earthRadius * c;
	}

	// 검색 조건에 따라 화장실 데이터를 필터링 🎀
	public List<Toilet> searchToilets(ToiletSearchCriteriaDto criteria) {
		List<Toilet> allToilets = toiletRepository.findAll();

		return allToilets.stream()
			.filter(toilet -> calculateDistance(criteria.getLatitude(), criteria.getLongitude(), toilet.getLatitude(), toilet.getLongitude()) <= criteria.getRadius())
			.filter(toilet -> criteria.getToiletName() == null || toilet.getToiletName().contains(criteria.getToiletName()))
			.filter(toilet -> criteria.getAddress() == null ||
				toilet.getAddressRoad().contains(criteria.getAddress()) ||
				toilet.getAddressJibun().contains(criteria.getAddress()))
			.filter(toilet -> criteria.getDetailedOpeningHours() == null || isWithinOperatingHours(toilet.getDetailedOpeningHours(), criteria.getDetailedOpeningHours()))
			.filter(toilet -> criteria.getCctv() == null || toilet.getCctv().equals(criteria.getCctv() ? "Y" : "N"))
			.filter(toilet -> criteria.getDisabledFacility() == null ||
				(criteria.getDisabledFacility() && toilet.getMaleDisabledToiletCount() > 0 && toilet.getFemaleDisabledToiletCount() > 0))
			.collect(Collectors.toList());
	}

	// 특정 시간대 운영 여부 판단 🎀
	private boolean isWithinOperatingHours(String toiletHours, String requestedHours) {
		// 예: "09:00-18:00"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		try {
			String[] toiletTimeRange = toiletHours.split("-");
			String[] requestedTimeRange = requestedHours.split("-");

			LocalTime toiletStart = LocalTime.parse(toiletTimeRange[0], formatter);
			LocalTime toiletEnd = LocalTime.parse(toiletTimeRange[1], formatter);

			LocalTime requestedStart = LocalTime.parse(requestedTimeRange[0], formatter);
			LocalTime requestedEnd = LocalTime.parse(requestedTimeRange[1], formatter);

			return !requestedStart.isBefore(toiletStart) && !requestedEnd.isAfter(toiletEnd);
		} catch (Exception e) {
			return false;  // 파싱 에러가 발생할 경우 false 반환
		}
	}

}
