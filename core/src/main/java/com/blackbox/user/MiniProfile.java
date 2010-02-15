package com.blackbox.user;

import com.blackbox.activity.IActivity;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 *
 */
@XmlRootElement
@XmlAccessorType
public class MiniProfile extends User implements Serializable {

    private static final long serialVersionUID = -3502732334401886744L;
    private IActivity activity;
    private int totalVouches;
    private String locationCity;
    private String locationState;
    private String currentCity;
    private boolean initialized;
    private String lookingForExplain;

    public MiniProfile() {
        super(null);
    }

    public MiniProfile(String username, String guid, String name, String lastname, String locationCity, String locationState, String currentCity, DateTime lastOnline, int totalVouches, String lookingForExplain) {
        super();
        this.totalVouches = totalVouches;
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.currentCity = currentCity;
        setUsername(username);
        setGuid(guid);
        setName(name);
        setLastname(lastname);
        setLastOnline(lastOnline);
        setProfile(null);
    }

    public MiniProfile(String username, String guid, String name, String lastname, String locationCity, String locationState, String currentCity, DateTime lastOnline, String lookingForExplain) {
        super();
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.currentCity = currentCity;
        setUsername(username);
        setGuid(guid);
        setName(name);
        setLastname(lastname);
        setLastOnline(lastOnline);
        setProfile(null);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public int getTotalVouches() {
        return totalVouches;
    }

    public void setTotalVouches(int totalVouches) {
        this.totalVouches = totalVouches;
    }

    public IActivity getActivity() {
        return activity;
    }

    public void setActivity(IActivity activity) {
        this.activity = activity;
    }

    public String getLookingForExplain() {
        return lookingForExplain;
    }

    public void setLookingForExplain(String lookingForExplain) {
        this.lookingForExplain = lookingForExplain;
    }

    @Override
    public String toString() {
        return "MiniProfile{" +
                "activity=" + activity +
                ", totalVouches=" + totalVouches +
                ", locationCity='" + locationCity + '\'' +
                ", locationState='" + locationState + '\'' +
                ", currentCity='" + currentCity + '\'' +
                ", initialized=" + initialized +
                ", lookingForExplain='" + lookingForExplain + '\'' +
                '}';
    }
}
