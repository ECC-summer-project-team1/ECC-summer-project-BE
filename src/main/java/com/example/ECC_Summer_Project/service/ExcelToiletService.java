package com.example.ECC_Summer_Project.service;

import com.example.ECC_Summer_Project.model.Toilet;
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

                // 남성용-장애인용 대변기 수와 여성용-장애인용 대변기 수를 확인하여 장애인 시설 여부를 설정
                int maleDisabledToiletCount = (int) getNumericCellValue(row.getCell(9));
                int femaleDisabledToiletCount = (int) getNumericCellValue(row.getCell(13));
                toilet.setDisabledFacility(maleDisabledToiletCount > 0 || femaleDisabledToiletCount > 0);

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
     * 필터링 조건에 맞는 화장실 리스트를 반환하는 메서드
     *
     * @param filePath 엑셀 파일의 경로
     * @param disabledFacility 장애인 시설 여부 필터링 조건
     * @param operatingHours 운영 시간 필터링 조건
     * @return 필터링된 화장실 정보 리스트
     */
    public List<Toilet> filterToilets(String filePath, Boolean disabledFacility, String operatingHours) {
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
                .filter(toilet -> (disabledFacility == null || toilet.isDisabledFacility() == disabledFacility)
                        && (operatingHours == null || toilet.getOperatingHours().equals(operatingHours)))
                .collect(Collectors.toList());

        System.out.println("Filtered toilets count: " + filteredToilets.size());
        return filteredToilets;
    }
}
