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
     * 화장실 정보를 필터링하여 반환하는 엔드포인트
     *
     * @param disabledFacility 장애인 시설 여부 필터링 조건
     * @param operatingHours 운영 시간 필터링 조건
     * @return 필터링된 화장실 정보 리스트
     */
    @GetMapping
    public List<Toilet> getToilets(
            @RequestParam(value = "disabledFacility", required = false) Boolean disabledFacility,
            @RequestParam(value = "operatingHours", required = false) String operatingHours) {

        // 엑셀 파일 경로 설정
        String filePath = "static/공공화장실정보.xlsx"; // 엑셀 파일이 위치한 경로

        // 필터링된 화장실 정보를 반환
        return excelToiletService.filterToilets(filePath, disabledFacility, operatingHours);
    }
}
