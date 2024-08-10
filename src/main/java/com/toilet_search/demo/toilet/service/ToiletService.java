package com.toilet_search.demo.toilet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toilet_search.demo.toilet.model.Toilet;

@Service
public class ToiletService {

	// 주소 기반 검색
	public List<Toilet> searchToiletsByAddress(MultipartFile file, String address) {
		return searchToiletsInExcel(file, address, null, null, null);
	}

	// 위도, 경도 및 반경 기반 검색
	public List<Toilet> searchToiletsByLocation(MultipartFile file, double userLat, double userLon, double radius) {
		return searchToiletsInExcel(file, null, userLat, userLon, radius);
	}

	// 내부적으로 검색 조건에 따라 분기 처리
	private List<Toilet> searchToiletsInExcel(MultipartFile file, String address, Double userLat, Double userLon, Double radius) {
		List<Toilet> toiletList = new ArrayList<>();

		try {
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);

				// 주소 기반 검색
				if (address != null) {
					String roadAddress = row.getCell(4).getStringCellValue();
					String jibunAddress = row.getCell(5).getStringCellValue();
					if (roadAddress.contains(address) || jibunAddress.contains(address)) {
						toiletList.add(buildToilet(row));
					}
				}
				// 위치 기반 검색
				else if (userLat != null && userLon != null && radius != null) {
					// double toiletLat = Double.parseDouble(row.getCell(20).getStringCellValue());
					double toiletLat= getCellValueAsDouble(row.getCell(20));
					// double toiletLon = Double.parseDouble(row.getCell(21).getStringCellValue());
					double toiletLon = getCellValueAsDouble(row.getCell(21));
					double distance = calculateDistance(userLat, userLon, toiletLat, toiletLon);
					if (distance <= radius) {
						toiletList.add(buildToilet(row));
					}
				}
			}
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("엑셀 파일을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
		}

		return toiletList;
	}

	private static double calculateDistance(double userLat, double userLon, double toiletLat, double toiletLon) {

		double theta = userLon - toiletLon;
		double dist = Math.sin(deg2rad(userLat)) * Math.sin(deg2rad(toiletLat)) + Math.cos(deg2rad(userLat)) * Math.cos(deg2rad(toiletLat)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1609.344;

		return dist;
	}
	// This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	// 셀 값을 Double로 안전하게 가져오는 메서드
	private double getCellValueAsDouble(Cell cell) {
		if (cell == null) {
			return 0.0; // 기본값 반환
		}
		try {
			return Double.parseDouble(cell.getStringCellValue());
		} catch (NumberFormatException e) {
			return 0.0; // 기본값 반환
		}
	}

	// Toilet 객체를 생성하는 빌더 메서드
	private Toilet buildToilet(Row row) {
		return Toilet.builder()
			.category(row.getCell(1).getStringCellValue())
			.reference(row.getCell(2).getStringCellValue())
			.toiletName(row.getCell(3).getStringCellValue())
			.addressRoad(row.getCell(4).getStringCellValue())
			.addressJibun(row.getCell(5).getStringCellValue())
			.maleToiletCount(Integer.parseInt(row.getCell(6).getStringCellValue()))
			.maleUrinalCount(Integer.parseInt(row.getCell(7).getStringCellValue()))
			.maleDisabledToiletCount(Integer.parseInt(row.getCell(8).getStringCellValue()))
			.maleDisabledUrinalCount(Integer.parseInt(row.getCell(9).getStringCellValue()))
			.maleChildToiletCount(Integer.parseInt(row.getCell(10).getStringCellValue()))
			.maleChildUrinalCount(Integer.parseInt(row.getCell(11).getStringCellValue()))
			.femaleToiletCount(Integer.parseInt(row.getCell(12).getStringCellValue()))
			.femaleDisabledToiletCount(Integer.parseInt(row.getCell(13).getStringCellValue()))
			.femaleChildToiletCount(Integer.parseInt(row.getCell(14).getStringCellValue()))
			.managingInstitution(row.getCell(15).getStringCellValue())
			.phoneNumber(row.getCell(16).getStringCellValue())
			.openingHours(row.getCell(17).getStringCellValue())
			.detailedOpeningHours(row.getCell(18).getStringCellValue())
			.installationDate(row.getCell(19).getStringCellValue())
			.latitude(Double.parseDouble(row.getCell(20).getStringCellValue()))
			.longitude(Double.parseDouble(row.getCell(21).getStringCellValue()))
			.ownershipType(row.getCell(22).getStringCellValue())
			.wasteManagementType(row.getCell(23).getStringCellValue())
			.safetyFacilityRequired(row.getCell(24).getStringCellValue())
			.emergencyBell(row.getCell(25).getStringCellValue())
			.emergencyBellLocation(row.getCell(26).getStringCellValue())
			.cctv(row.getCell(27).getStringCellValue())
			.diaperChangingStation(row.getCell(28).getStringCellValue())
			.diaperChangingStationLocation(row.getCell(29).getStringCellValue())
			.remodelingDate(row.getCell(30).getStringCellValue())
			.dataStandardDate(row.getCell(31).getStringCellValue())
			.build();
	}

}
