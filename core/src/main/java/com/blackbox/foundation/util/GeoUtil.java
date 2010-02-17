package com.blackbox.foundation.util;


import com.blackbox.foundation.geo.GeoPoint;
import com.blackbox.foundation.social.Address;
import geo.google.GeoAddressStandardizer;
import geo.google.GeoException;
import geo.google.datamodel.GeoCoordinate;
import geo.google.datamodel.GeoUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Utility to get the geolocation of an address, and
 * calculate the distance between two geolocations.
 * Defines miles per degrees latitude and longitude, but
 * it uses a more accurate distance calculation in
 * the geo.google package.
 */
public class GeoUtil implements IGeoUtil {
    final private static Logger logger = LoggerFactory.getLogger(GeoUtil.class);

    /**
     * At the equator, 1 degree of latitude is 68.7 miles, and at the poles,
     * 1 degree of latitude is 69.4 miles, so 69.05 is our flat earth average.
     */
    public static final double MILES_PER_DEGREE_LATITUDE = 69.05;

    /**
     * Miles per degree longitude varies depending on the latitude.
     * A reasonable approximation is
     * 69.4 * cos(average latitude) * delta longitude
     * Since a good portion of our use base will be around major US
     * cities, we'll use a value for 40 degrees.  (And I haven't gotten
     * that formula to work right.)
     * <p/>
     * Around Portland, it would be closer ot 48.62 miles.
     * San Francisco, 54.85.
     * San Diego, 58.38
     * For now, averaging out at 40 degrees. We could put a table
     * in, to get a value for each degree, and that would tighten the
     * calc up significantly without adding much to the calculation time.
     */
    public static final double MILES_PER_DEGREE_LONGITUDE = 53.16;


    public GeoAddressStandardizer gas;
    private String googleKey;

    public GeoAddressStandardizer getGas() {
        return gas;
    }

    public void setGas(GeoAddressStandardizer gas) {
        this.gas = gas;
    }


    public GeoUtil() {
        setGas(new GeoAddressStandardizer(googleKey, 0));
    }

    public boolean isValid(GeoPoint geoPoint) {
        return geoPoint != null && geoPoint.getLatitude() != null && geoPoint.getLongitude() != null;
    }

    public Double findDistanceInMiles(GeoPoint a, GeoPoint b) {
        if (!isValid(a) || !isValid(b)) {
            return null;
        }
        return GeoUtils.distanceBetweenInMiles(
                new GeoCoordinate(
                        a.getLongitude() / GeoPoint.GEOPOINT_MULTIPLIER,
                        a.getLatitude() / GeoPoint.GEOPOINT_MULTIPLIER,
                        null),
                new GeoCoordinate(
                        b.getLongitude() / GeoPoint.GEOPOINT_MULTIPLIER,
                        b.getLatitude() / GeoPoint.GEOPOINT_MULTIPLIER,
                        null)
        );
    }


    public GeoPoint fetchGeoInfoForAddress(Address addr) {
        GeoPoint result = new GeoPoint(null, null);

        StringBuffer sb = new StringBuffer("");
        if (appendString(sb, addr.getAddress1())) sb.append(", ");
        if (appendString(sb, addr.getAddress2())) sb.append(", ");
        if (appendString(sb, addr.getCity())) sb.append(", ");
        if (appendString(sb, addr.getCounty())) sb.append(", ");
        if (appendString(sb, addr.getState())) sb.append(", ");

        if (addr.getCountry() != null && appendString(sb, addr.getCountry().toString())) sb.append(" ");
        appendString(sb, addr.getZipCode());
        if (sb.length() == 0) {
            return result;
        }

        try {
            GeoCoordinate gc = getGas().standardizeToGeoCoordinate(sb.toString());
            result.setLatitude(Math.round(gc.getLatitude() * GeoPoint.GEOPOINT_MULTIPLIER));
            result.setLongitude(Math.round(gc.getLongitude() * GeoPoint.GEOPOINT_MULTIPLIER));
        } catch (GeoException e) {
            logger.warn("Address not decoded: " + sb.toString(), e);
        }

        return result;
    }

    public GeoPoint calculatePointInMilesNE(GeoPoint from, int miles) {
        GeoPoint result = new GeoPoint(null, null);
        if (!isValid(from)) return result;
        Long deltaLongitude =
                Math.round(miles / MILES_PER_DEGREE_LONGITUDE * GeoPoint.GEOPOINT_MULTIPLIER);
        Long deltaLatitude =
                Math.round(miles / MILES_PER_DEGREE_LATITUDE * GeoPoint.GEOPOINT_MULTIPLIER);
        result.setLatitude(from.getLatitude() + deltaLatitude);
        result.setLongitude(from.getLongitude() + deltaLongitude);
        return result;
    }

    public GeoPoint calculatePointInMilesSW(GeoPoint from, int miles) {
        GeoPoint result = new GeoPoint(null, null);
        if (!isValid(from)) return result;
        Long deltaLongitude =
                Math.round(miles / MILES_PER_DEGREE_LONGITUDE * GeoPoint.GEOPOINT_MULTIPLIER);
        Long deltaLatitude =
                Math.round(miles / MILES_PER_DEGREE_LATITUDE * GeoPoint.GEOPOINT_MULTIPLIER);
        result.setLatitude(from.getLatitude() - deltaLatitude);
        result.setLongitude(from.getLongitude() - deltaLongitude);
        return result;
    }

    public GeoPoint fetchGeoInfoForZipCode(String zip) {
        GeoPoint geoPoint = new GeoPoint(null, null);
        if (StringUtils.isBlank(zip)) {
            return geoPoint;
        }

        try {
            GeoCoordinate gc = getGas().standardizeToGeoCoordinate(zip);
            geoPoint.setLatitude(Math.round(gc.getLatitude() * GeoPoint.GEOPOINT_MULTIPLIER));
            geoPoint.setLongitude(Math.round(gc.getLongitude() * GeoPoint.GEOPOINT_MULTIPLIER));
        } catch (GeoException e) {
            logger.warn("Zip code not decoded: " + zip, e);
        }
        return geoPoint;
    }

    @Required
    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    private boolean appendString(StringBuffer sb, String line) {
        if (line != null && line.length() > 0) {
            sb.append(line);
            return true;
        }
        return false;
    }

}
