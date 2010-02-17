package com.blackbox.foundation.user;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

import java.io.Serializable;


@Searchable(root = false)
public class LookingFor implements Serializable {

    @SearchableProperty
    private boolean dates, hookup, friends, love, donkeySex, snuggling;


    public boolean isDates() {
        return dates;
    }

    public void setDates(boolean dates) {
        this.dates = dates;
    }

    public boolean isHookup() {
        return hookup;
    }

    public void setHookup(boolean hookup) {
        this.hookup = hookup;
    }

    public boolean isFriends() {
        return friends;
    }

    public void setFriends(boolean friends) {
        this.friends = friends;
    }

    public boolean isLove() {
        return love;
    }

    public void setLove(boolean love) {
        this.love = love;
    }

    /**
     * now relationship
     */
    public boolean isDonkeySex() {
        return donkeySex;
    }

    public void setDonkeySex(boolean donkeySex) {
        this.donkeySex = donkeySex;
    }

    public boolean isSnuggling() {
        return snuggling;
    }

    public void setSnuggling(boolean snuggling) {
        this.snuggling = snuggling;
    }

}
