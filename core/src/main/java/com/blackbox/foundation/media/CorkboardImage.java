package com.blackbox.foundation.media;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.user.User;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class CorkboardImage extends BBPersistentObject {

    private EntityReference owner;
    private String metaDataGuid;
    private int x;
    private int y;
    private int z;
    private int rotation;
    private double scale = 1.0;
    private String location;
    private String fileName;

    public CorkboardImage(EntityReference owner, String metaDataGuid, int x, int y, int z,
                          int rotation, double scale) {
        this.owner = owner;
        this.metaDataGuid = metaDataGuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.scale = scale;
    }

    public CorkboardImage() {
    }

    public EntityReference getOwner() {
        return owner;
    }

    public void setOwner(EntityReference owner) {
        this.owner = owner;
    }

    public String getMetaDataGuid() {
        return metaDataGuid;
    }

    public void setMetaDataGuid(String metaDataGuid) {
        this.metaDataGuid = metaDataGuid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static CorkboardImage createCorkboardImage() {
        CorkboardImage ci = new CorkboardImage();
        Utils.applyGuid(ci);
        return ci;
    }

    public static CorkboardImage createCorkboadImage(User owner, MediaMetaData metaData, int x, int y, int z,
                                                     int rotation, double scale) {
        CorkboardImage ci = new CorkboardImage(owner.toEntityReference(), metaData.getGuid(), x, y, z, rotation, scale);
        Utils.applyGuid(ci);
        return ci;
    }

    public static CorkboardImage createCorkboadImage(User owner, MediaMetaData metaData) {
        CorkboardImage ci = new CorkboardImage(owner.toEntityReference(), metaData.getGuid(), 0, 0, 0, 0, 1);
        Utils.applyGuid(ci);
        return ci;
    }

}
