package com.blackbox.foundation.util;

import com.blackbox.foundation.geo.GeoPoint;
import com.blackbox.foundation.social.Address;


/**
 * Class for acquiring geo information
 */
public interface IGeoUtil {

    /**
     * @param geoPoint a geopoint
     * @return true if the value and its lat/long values are not null
     */
    boolean isValid(GeoPoint geoPoint);

    /**
     * Calculates the distance in miles between two points.
     * Returns null if either value is invalid.
     * (Could return a value larger than 12000 if that's
     * better for sorting purposes.)
     *
     * @param a a geopoint
     * @param b another geopoint
     * @return distance in miles
     */
    Double findDistanceInMiles(GeoPoint a, GeoPoint b);

    /**
     * Find the latitude and longitude for a given address.
     * Currently uses Google.  If an error occurs, or the service
     * can't find a lat/long, the GeoPoint returned contains
     * nulls for its settings.
     *
     * @param addr Profile.currentAddress
     * @return a GeoPoint with the result of fetching an address
     */
    GeoPoint fetchGeoInfoForAddress(Address addr);

    /**
     * Only latitude is reasonably accurate.
     * Depending on the latitude, the
     *
     * @param from  originating point
     * @param miles distance away in the circle
     * @return a geopoint that is x miles away to the NE
     */
    GeoPoint calculatePointInMilesNE(GeoPoint from, int miles);

    /**
     * Only latitude is reasonably accurate.
     * Depending on the latitude, the
     *
     * @param from  originating point
     * @param miles distance away in the circle
     * @return a geopoint that is x miles away to the SW
     */
    GeoPoint calculatePointInMilesSW(GeoPoint from, int miles);

    /**
     * Find the latitude and longitude for a given zip code.
     * Currently uses Google.  If an error occurs, or the service
     * can't find a lat/long, the GeoPoint returned contains
     * nulls for its settings.
     *
     * @param zip a zip code
     * @return a GeoPoint with the result of fetching a zip
     */
    GeoPoint fetchGeoInfoForZipCode(String zip);
}
