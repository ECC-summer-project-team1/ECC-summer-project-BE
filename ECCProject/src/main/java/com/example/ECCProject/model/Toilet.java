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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Toilet toilet = (Toilet) o;
        return Double.compare(toilet.latitude, latitude) == 0 &&
                Double.compare(toilet.longitude, longitude) == 0 &&
                disabledFacility == toilet.disabledFacility &&
                Objects.equals(name, toilet.name) &&
                Objects.equals(address, toilet.address) &&
                Objects.equals(operatingHours, toilet.operatingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, latitude, longitude, operatingHours, disabledFacility);
    }
}
