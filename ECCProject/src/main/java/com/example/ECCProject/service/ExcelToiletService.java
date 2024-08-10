package com.example.ECCProject.service;

import com.example.ECCProject.model.Toilet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 엑셀 파일에서 데이터를 읽어와 처리하는 서비스 클래스
 */
@Service
public class ExcelToiletService {

    /**
     * 엑셀 파일의 데이터를 읽어서 화장실 객체 리스트로 반환하는 메서드
     *
     * @param filePath 엑셀 파일의 경로
     * @return 화장실 정보 리스트
     */
    public List<Toilet> readToiletData(String filePath) throws IOException {
        List<Toilet> toilets = new ArrayList<>();

        // 엑셀 파일을 읽어들임
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        try (InputStream inputStream = classPathResource.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // 첫 번째 시트를 가져옴
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 각 행을 순회함
            for (Row row : sheet) {
                // 첫 번째 행은 헤더이므로 건너뜀
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Toilet 객체를 생성하고 엑셀 데이터로 초기화
                Toilet toilet = new Toilet();
                toilet.setName(getStringCellValue(row.getCell(3)));
                toilet.setAddress(getStringCellValue(row.getCell(4)));
                toilet.setLatitude(getNumericCellValue(row.getCell(20)));
                toilet.setLongitude(getNumericCellValue(row.getCell(21)));
                toilet.setOperatingHours(getStringCellValue(row.getCell(18)));

                // 남성용-장애인용 대변기, 소변기 수와 여성용-장애인용 대변기 수를 확인하여 장애인 시설 여부를 설정
                int maleDisabledToiletCount = (int) getNumericCellValue(row.getCell(8)); // 남성용 장애인 대변기 수
                int femaleDisabledToiletCount = (int) getNumericCellValue(row.getCell(13)); // 여성용 장애인 대변기 수
                int maleDisabledUrinalCount = (int) getNumericCellValue(row.getCell(9)); // 남성용 장애인 소변기 수
                toilet.setMaleDisabledToilet(maleDisabledToiletCount > 0);
                toilet.setFemaleDisabledToilet(femaleDisabledToiletCount > 0);
                toilet.setMaleDisabledUrinal(maleDisabledUrinalCount > 0);

                // 남성용-어린이용 변기 수와 여성용-어린이용 대변기 수를 확인하여 어린이 시설 여부를 설정
                int maleChildToiletCount = (int) getNumericCellValue(row.getCell(10));
                int femaleChildToiletCount = (int) getNumericCellValue(row.getCell(14));
                int maleChildUrinalCount = (int) getNumericCellValue(row.getCell(11));
                toilet.setMaleChildFacility(maleChildToiletCount > 0);
                toilet.setFemaleChildFacility(femaleChildToiletCount > 0);
                toilet.setMaleChildUrinal(maleChildUrinalCount > 0);

                // CCTV 설치 여부, 비상벨 설치 여부, 기저귀 교환대 유무를 설정
                toilet.setCctvInstalled(getBooleanCellValue(row.getCell(27)));
                toilet.setEmergencyBellInstalled(getBooleanCellValue(row.getCell(25)));
                toilet.setDiaperChangingStation(getBooleanCellValue(row.getCell(28)));

                // Toilet 객체를 리스트에 추가
                toilets.add(toilet);
            }

        } catch (IOException e) {
            // 파일 읽기 도중 발생하는 예외를 처리
            e.printStackTrace();
            throw e;
        }

        System.out.println("Total toilets read: " + toilets.size());
        return toilets;
    }

    /**
     * 셀의 문자열 값을 가져오는 메서드
     *
     * @param cell 셀 객체
     * @return 문자열 값
     */
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    /**
     * 셀의 숫자 값을 가져오는 메서드
     *
     * @param cell 셀 객체
     * @return 숫자 값
     */
    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }
        try {
            return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : Double.parseDouble(cell.getStringCellValue());
        } catch (NumberFormatException e) {
            return 0;  // 빈 문자열이나 변환할 수 없는 문자열을 0으로 처리
        }
    }

    /**
     * 셀의 값을 Boolean으로 변환하는 메서드 (Y/N을 true/false로 변환)
     *
     * @param cell 셀 객체
     * @return Boolean 값 (Y/N -> true/false)
     */
    private boolean getBooleanCellValue(Cell cell) {
        if (cell == null) {
            return false;  // 셀이 비어있으면 기본값 false
        }
        return "Y".equalsIgnoreCase(cell.getStringCellValue());
    }

    /**
     * 필터링 조건에 맞는 화장실 리스트를 반환하는 메서드
     *
     * @param filePath 엑셀 파일의 경로
     * @param maleDisabledFacility 남성용 장애인 시설 여부 필터링 조건
     * @param femaleDisabledFacility 여성용 장애인 시설 여부 필터링 조건
     * @param maleDisabledUrinal 남성용 장애인 소변기 시설 여부 필터링 조건
     * @param maleChildFacility 남성용 어린이 시설 여부 필터링 조건
     * @param femaleChildFacility 여성용 어린이 시설 여부 필터링 조건
     * @param maleChildUrinal 남성용 어린이 소변기 시설 여부 필터링 조건
     * @param cctvInstalled CCTV 설치 여부 필터링 조건
     * @param emergencyBellInstalled 비상벨 설치 여부 필터링 조건
     * @param diaperChangingStation 기저귀 교환대 유무 필터링 조건
     * @param operatingHours 운영 시간 필터링 조건
     * @return 필터링된 화장실 정보 리스트
     */
    public List<Toilet> filterToilets(String filePath, Boolean maleDisabledFacility, Boolean femaleDisabledFacility, Boolean maleDisabledUrinal, Boolean maleChildFacility, Boolean femaleChildFacility, Boolean maleChildUrinal, Boolean cctvInstalled, Boolean emergencyBellInstalled, Boolean diaperChangingStation, String operatingHours) {
        // 엑셀 파일에서 모든 화장실 데이터를 읽어옴
        List<Toilet> toilets = new ArrayList<>();
        try {
            toilets = readToiletData(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();  // 예외 발생 시 빈 리스트 반환
        }

        // 필터링 조건에 맞는 화장실 리스트를 반환
        List<Toilet> filteredToilets = toilets.stream()
                .filter(toilet -> (maleDisabledFacility == null || toilet.isMaleDisabledToilet() == maleDisabledFacility)
                        && (femaleDisabledFacility == null || toilet.isFemaleDisabledToilet() == femaleDisabledFacility)
                        && (maleDisabledUrinal == null || toilet.isMaleDisabledUrinal() == maleDisabledUrinal)
                        && (maleChildFacility == null || toilet.isMaleChildFacility() == maleChildFacility)
                        && (femaleChildFacility == null || toilet.isFemaleChildFacility() == femaleChildFacility)
                        && (maleChildUrinal == null || toilet.isMaleChildUrinal() == maleChildUrinal)
                        && (cctvInstalled == null || toilet.isCctvInstalled() == cctvInstalled)
                        && (emergencyBellInstalled == null || toilet.isEmergencyBellInstalled() == emergencyBellInstalled)
                        && (diaperChangingStation == null || toilet.isDiaperChangingStation() == diaperChangingStation)
                        && (operatingHours == null || toilet.getOperatingHours().equals(operatingHours)))
                .collect(Collectors.toList());

        System.out.println("필터링된 화장실 개수: " + filteredToilets.size());
        return filteredToilets;
    }
}
