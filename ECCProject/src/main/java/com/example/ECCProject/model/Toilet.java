package com.example.ECCProject.model;

import java.util.Objects;

/**
 * 화장실 정보를 나타내는 모델 클래스
 */
public class Toilet {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String operatingHours;

    private boolean maleDisabledToilet;
    private boolean femaleDisabledToilet;
    private boolean maleDisabledUrinal;
    private boolean maleChildFacility;
    private boolean femaleChildFacility;
    private boolean maleChildUrinal;

    private boolean cctvInstalled;         // CCTV 설치 여부
    private boolean emergencyBellInstalled; // 비상벨 설치 여부
    private boolean diaperChangingStation;  // 기저귀 교환대 유무

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

    public boolean isMaleDisabledToilet() {
        return maleDisabledToilet;
    }

    public void setMaleDisabledToilet(boolean maleDisabledToilet) {
        this.maleDisabledToilet = maleDisabledToilet;
    }

    public boolean isFemaleDisabledToilet() {
        return femaleDisabledToilet;
    }

    public void setFemaleDisabledToilet(boolean femaleDisabledToilet) {
        this.femaleDisabledToilet = femaleDisabledToilet;
    }

    public boolean isMaleDisabledUrinal() {
        return maleDisabledUrinal;
    }

    public void setMaleDisabledUrinal(boolean maleDisabledUrinal) {
        this.maleDisabledUrinal = maleDisabledUrinal;
    }

    public boolean isMaleChildFacility() {
        return maleChildFacility;
    }

    public void setMaleChildFacility(boolean maleChildFacility) {
        this.maleChildFacility = maleChildFacility;
    }

    public boolean isFemaleChildFacility() {
        return femaleChildFacility;
    }

    public void setFemaleChildFacility(boolean femaleChildFacility) {
        this.femaleChildFacility = femaleChildFacility;
    }

    public boolean isMaleChildUrinal() {
        return maleChildUrinal;
    }

    public void setMaleChildUrinal(boolean maleChildUrinal) {
        this.maleChildUrinal = maleChildUrinal;
    }

    public boolean isCctvInstalled() {
        return cctvInstalled;
    }

    public void setCctvInstalled(boolean cctvInstalled) {
        this.cctvInstalled = cctvInstalled;
    }

    public boolean isEmergencyBellInstalled() {
        return emergencyBellInstalled;
    }

    public void setEmergencyBellInstalled(boolean emergencyBellInstalled) {
        this.emergencyBellInstalled = emergencyBellInstalled;
    }

    public boolean isDiaperChangingStation() {
        return diaperChangingStation;
    }

    public void setDiaperChangingStation(boolean diaperChangingStation) {
        this.diaperChangingStation = diaperChangingStation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Toilet toilet = (Toilet) o;
        return Double.compare(toilet.latitude, latitude) == 0 &&
                Double.compare(toilet.longitude, longitude) == 0 &&
                maleDisabledToilet == toilet.maleDisabledToilet &&
                femaleDisabledToilet == toilet.femaleDisabledToilet &&
                maleDisabledUrinal == toilet.maleDisabledUrinal &&
                maleChildFacility == toilet.maleChildFacility &&
                femaleChildFacility == toilet.femaleChildFacility &&
                maleChildUrinal == toilet.maleChildUrinal &&
                cctvInstalled == toilet.cctvInstalled &&
                emergencyBellInstalled == toilet.emergencyBellInstalled &&
                diaperChangingStation == toilet.diaperChangingStation &&
                Objects.equals(name, toilet.name) &&
                Objects.equals(address, toilet.address) &&
                Objects.equals(operatingHours, toilet.operatingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, latitude, longitude, operatingHours,
                maleDisabledToilet, femaleDisabledToilet, maleDisabledUrinal,
                maleChildFacility, femaleChildFacility, maleChildUrinal,
                cctvInstalled, emergencyBellInstalled, diaperChangingStation);
    }
}