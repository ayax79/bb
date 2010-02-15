package com.blackbox.server.util;

import com.blackbox.geo.GeoPoint;
import com.blackbox.search.ExploreRequest;
import com.blackbox.util.Affirm;
import com.blackbox.util.GeoUtil;

/**
 * @author colin@blackboxrepublic.com
 */
public class GeoHelper {

    /**
     * This (very coupled) method, fetches the zipCode from the exploreRequest and either builds up the GeoPoint data for
     * that zip code <strong>and sets</strong> that onto the exploreRequest or <strong>UN</strong>sets those data on the
     * exploreRequest.
     *
     * @throws IllegalArgumentException if exploreRequest is null
     * @precondition exploreRequest is not null
     * @postcondition exploreRequest request either has valid geoPoint data for neCorner and swCorner or has those
     * attributes set to null
     */
    public static void applyGeoDataBasedOnExploreRequestSituation(ExploreRequest exploreRequest) {
        Affirm.isNotNull(exploreRequest, "exploreRequest", IllegalArgumentException.class);
        GeoUtil geoUtil = new GeoUtil();
        GeoPoint zipPoint = geoUtil.fetchGeoInfoForZipCode(exploreRequest.getZipCode());
        // flat-earth based rectangle to determine min/max lat & long values to search on.
        if (geoUtil.isValid(zipPoint)) {
            GeoPoint ne = geoUtil.calculatePointInMilesNE(zipPoint, exploreRequest.getMaxDistance());
            GeoPoint sw = geoUtil.calculatePointInMilesSW(zipPoint, exploreRequest.getMaxDistance());
            exploreRequest.setNeCorner(ne);
            exploreRequest.setSwCorner(sw);
        } else {
            exploreRequest.setNeCorner(null);
            exploreRequest.setSwCorner(null);
        }
    }
}
