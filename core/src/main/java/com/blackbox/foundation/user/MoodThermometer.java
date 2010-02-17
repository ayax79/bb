package com.blackbox.foundation.user;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Searchable(root = false)
public class MoodThermometer implements Serializable {

    public static final int MAX = 2;
    public static final int MID = 0;
    public static final int MIN = -2;

    @SearchableProperty
    private int orientation = 0; // slider
    @SearchableProperty
    private int polyStatus = 0;  // slider
    @SearchableProperty
    private int relationshipStatus = 0;
    @SearchableProperty
    private int interestLevel = 0;
    @SearchableProperty
    private int energyLevel = 0;
    @SearchableProperty
    private boolean makePrivate;
    private static final long serialVersionUID = -3046630129071655049L;

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getPolyStatus() {
        return polyStatus;
    }

    public void setPolyStatus(int polyStatus) {
        this.polyStatus = polyStatus;
    }

    public int getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(int relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public int getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(int interestLevel) {
        this.interestLevel = interestLevel;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public boolean isMakePrivate() {
        return makePrivate;
    }

    public void setMakePrivate(boolean makePrivate) {
        this.makePrivate = makePrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoodThermometer moodThermometer = (MoodThermometer) o;

        if (energyLevel != moodThermometer.energyLevel) return false;
        if (interestLevel != moodThermometer.interestLevel) return false;
        if (makePrivate != moodThermometer.makePrivate) return false;
        if (orientation != moodThermometer.orientation) return false;
        if (polyStatus != moodThermometer.polyStatus) return false;
        //noinspection RedundantIfStatement
        if (relationshipStatus != moodThermometer.relationshipStatus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orientation;
        result = 31 * result + polyStatus;
        result = 31 * result + relationshipStatus;
        result = 31 * result + interestLevel;
        result = 31 * result + energyLevel;
        result = 31 * result + (makePrivate ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MoodThermometer{" +
                "orientation=" + orientation +
                ", polyStatus=" + polyStatus +
                ", relationshipStatus=" + relationshipStatus +
                ", interestLevel=" + interestLevel +
                ", energyLevel=" + energyLevel +
                ", makePrivate=" + makePrivate +
                '}';
    }
}
