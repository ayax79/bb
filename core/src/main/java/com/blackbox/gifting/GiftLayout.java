package com.blackbox.gifting;

import com.blackbox.BBPersistentObject;
import com.blackbox.EntityReference;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GiftLayout extends BBPersistentObject {

    public static final String DEFAULT_FRAME = "default";

    public static enum Shelf {
        lower,
        upper
    }


    public static enum GiftLayoutSize {
        large,
        medium,
        small
    }


    private EntityReference activityReference;
    private int x;
    private int y;
    private int z;
    private GiftLayoutSize size = GiftLayoutSize.large;
    private ArtifactType type;
    private String body;
    private String iconLocation;
    private String location;
    private String frame = DEFAULT_FRAME;
    private Shelf shelf = Shelf.lower;
    private boolean active = true;

    public EntityReference getActivityReference() {
        return activityReference;
    }

    public void setActivityReference(EntityReference activityReference) {
        this.activityReference = activityReference;
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

    public GiftLayoutSize getSize() {
        return size;
    }

    public void setSize(GiftLayoutSize size) {
        this.size = size;
    }

    public ArtifactType getType() {
        return type;
    }

    public void setType(ArtifactType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIconLocation() {
        return iconLocation;
    }

    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GiftLayout that = (GiftLayout) o;

        if (active != that.active) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (activityReference != null ? !activityReference.equals(that.activityReference) : that.activityReference != null)
            return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (frame != null ? !frame.equals(that.frame) : that.frame != null) return false;
        if (iconLocation != null ? !iconLocation.equals(that.iconLocation) : that.iconLocation != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (shelf != that.shelf) return false;
        if (size != that.size) return false;
        //noinspection RedundantIfStatement
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (activityReference != null ? activityReference.hashCode() : 0);
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (iconLocation != null ? iconLocation.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (frame != null ? frame.hashCode() : 0);
        result = 31 * result + (shelf != null ? shelf.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GiftLayout{" +
                "activityReference=" + activityReference +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", size=" + size +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", iconLocation='" + iconLocation + '\'' +
                ", location='" + location + '\'' +
                ", frame='" + frame + '\'' +
                ", shelf=" + shelf +
                ", active=" + active +
                '}';
    }
}
