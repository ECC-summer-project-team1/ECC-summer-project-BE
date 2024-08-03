package com.example.ECC_Summer_Project.model;

/**
 * 화장실 정보를 나타내는 모델 클래스
 */
public class Toilet {
    // 화장실 이름
    private String name;
    // 화장실 주소
    private String address;
    // 위도
    private double latitude;
    // 경도
    private double longitude;
    // 운영 시간
    private String operatingHours;
    // 장애인 시설 여부
    private boolean disabledFacility;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public boolean isDisabledFacility() {
        return disabledFacility;
    }

    public void setDisabledFacility(boolean disabledFacility) {
        this.disabledFacility = disabledFacility;
    }
}