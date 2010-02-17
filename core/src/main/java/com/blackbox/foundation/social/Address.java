/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.social;

import com.blackbox.foundation.geo.GeoPoint;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.yestech.lib.i18n.CountryEnum;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "address")
@XStreamAlias("address")
@Searchable(root = false)
public class Address implements Serializable {
    private static final long serialVersionUID = 9071016672876781350L;

    @SearchableProperty
    private String address1;
    @SearchableProperty
    private String address2;
    @SearchableProperty
    private String city;
    @SearchableProperty
    private String state;
    @SearchableProperty
    private CountryEnum country;
    @SearchableProperty
    private String zipCode;
    @SearchableProperty
    private String county;
    @SearchableProperty
    private Long latitude;
    @SearchableProperty
    private Long longitude;
    @SearchableProperty
    private String timeZone;

    public Address() {
    }

    public Address(String address1, String address2, String city, String state, String zipCode, CountryEnum country) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Address(String address1, String address2, String city, String state, String zipCode, CountryEnum country, Long latitude, Long longitude) {
        this(address1, address2, city, state, zipCode, country);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    @XmlTransient
    public GeoPoint getGeoLocation() {
        return new GeoPoint(latitude, longitude);
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        if (geoLocation != null) {
            setLongitude(geoLocation.getLongitude());
            setLatitude(geoLocation.getLatitude());
        }
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
