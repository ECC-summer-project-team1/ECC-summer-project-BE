package com.example.ECC_Summer_Project.controller;

import com.example.ECC_Summer_Project.model.Toilet;
import com.example.ECC_Summer_Project.service.ExcelToiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 화장실 정보를 제공하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/api/toilets")
public class ToiletController {

    @Autowired
    private ExcelToiletService excelToiletService;

    /**
     * 필터링된 화장실 정보를 반환하는 API 엔드포인트
     *
     * @param disabledFacility 장애인 시설 여부 필터링 조건
     * @param operatingHours 운영 시간 필터링 조건
     * @return 필터링된 화장실 정보 리스트
     */
    @GetMapping("/filter")
    public List<Toilet> filterToilets(@RequestParam(required = false) Boolean disabledFacility,
                                      @RequestParam(required = false) String operatingHours) {
        // 엑셀 파일 경로
        String filePath = "공공화장실정보.xlsx"; // 리소스 파일 경로
        // 필터링된 화장실 정보 리스트를 반환
        return excelToiletService.filterToilets(filePath, disabledFacility, operatingHours);
    }
}
