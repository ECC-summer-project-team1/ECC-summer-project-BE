package com.toilet_search.demo.toilet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToiletSearchCriteriaDto {
	private double latitude;  // 필수
	private double longitude; // 필수
	private double radius;    // 필수
	private String toiletName; // 선택
	private String address;    // 선택
	private String detailedOpeningHours; // 선택, 특정 시간대
	private Boolean cctv;      // 선택, CCTV 유무
	private Boolean disabledFacility; // 선택, 장애인 시설 여부

}
