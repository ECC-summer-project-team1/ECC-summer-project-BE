package com.example.ECCProject.controller;

import com.example.ECCProject.model.Toilet;
import com.example.ECCProject.service.ExcelToiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 화장실 정보를 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/api/toilets")
public class ToiletController {

    @Autowired
    private ExcelToiletService excelToiletService;

    /**
     * 다양한 필터링 조건에 따라 화장실 정보를 반환하는 엔드포인트
     *
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
    @GetMapping
    public List<Toilet> getToilets(
            @RequestParam(value = "maleDisabledFacility", required = false) Boolean maleDisabledFacility,
            @RequestParam(value = "femaleDisabledFacility", required = false) Boolean femaleDisabledFacility,
            @RequestParam(value = "maleDisabledUrinal", required = false) Boolean maleDisabledUrinal,
            @RequestParam(value = "maleChildFacility", required = false) Boolean maleChildFacility,
            @RequestParam(value = "femaleChildFacility", required = false) Boolean femaleChildFacility,
            @RequestParam(value = "maleChildUrinal", required = false) Boolean maleChildUrinal,
            @RequestParam(value = "cctvInstalled", required = false) Boolean cctvInstalled,
            @RequestParam(value = "emergencyBellInstalled", required = false) Boolean emergencyBellInstalled,
            @RequestParam(value = "diaperChangingStation", required = false) Boolean diaperChangingStation,
            @RequestParam(value = "operatingHours", required = false) String operatingHours) {

        // 엑셀 파일 경로 설정
        String filePath = "static/공공화장실정보.xlsx";

        // 필터링된 화장실 정보를 반환
        return excelToiletService.filterToilets(filePath, maleDisabledFacility, femaleDisabledFacility,
                maleDisabledUrinal, maleChildFacility, femaleChildFacility, maleChildUrinal,
                cctvInstalled, emergencyBellInstalled, diaperChangingStation, operatingHours);
    }
}
