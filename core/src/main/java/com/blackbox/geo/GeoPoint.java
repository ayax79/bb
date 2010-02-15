package com.blackbox.geo;

import java.io.Serializable;

/**
 * Wrapper for latitude and longigude
 */
public class GeoPoint implements Serializable {

    public static double GEOPOINT_MULTIPLIER = 1000000000.0;

    private Long latitude;
    private Long longitude;
    private static final long serialVersionUID = -3765095427293814984L;

    protected GeoPoint() {

    }

    public GeoPoint(Long latitude, Long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoPoint(GeoPoint point) {
        this.latitude = point.getLatitude();
        this.longitude = point.getLongitude();
    }

    /**
     * @return latitude times 1,000,000, based on currentAddress
     */
    public Long getLatitude() {
        return latitude;
    }

    /**
     * @param latitude times 1,000,000, based on currentAddress
     */
    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    /**
     * @return longitude times 1,000,000, based on currentAddress
     */
    public Long getLongitude() {
        return longitude;
    }

    /**
     * @param longitude times 1,000,000, based on currentAddress
     */
    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public static boolean isMappable(GeoPoint geoPoint) {
        return geoPoint != null && geoPoint.getLatitude() != null && geoPoint.getLongitude() != null;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoPoint geoPoint = (GeoPoint) o;

        if (latitude != null ? !latitude.equals(geoPoint.latitude) : geoPoint.latitude != null) return false;
        if (longitude != null ? !longitude.equals(geoPoint.longitude) : geoPoint.longitude != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
