package com.blackbox.activity;

import com.blackbox.BBPersistentObject;
import com.blackbox.media.MediaMetaData;
import com.blackbox.message.Message;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlSeeAlso({MediaMetaData.class, Message.class})
public class TrainRockEntry extends BBPersistentObject {

    public static enum TrainwreckRockstarType {
        // don't change the order, hibernate is using the oridinal!
        TRAIN_WRECK,
        ROCKSTAR
    }

    protected int voteCount;
    protected TrainwreckRockstarType type;
    protected MediaMetaData parent;
    protected List<TrainRockComment> children = new ArrayList<TrainRockComment>();

    public MediaMetaData getParent() {
        return parent;
    }

    public void setParent(MediaMetaData parent) {
        this.parent = parent;
    }

    public List<TrainRockComment> getChildren() {
        return children;
    }

    public void setChildren(List<TrainRockComment> children) {
        this.children = children;
    }

    public TrainwreckRockstarType getType() {
        return type;
    }

    public void setType(TrainwreckRockstarType type) {
        this.type = type;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TrainRockEntry that = (TrainRockEntry) o;

        if (voteCount != that.voteCount) return false;
        if (children != null ? !children.equals(that.children) : that.children != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        //noinspection RedundantIfStatement
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + voteCount;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrainwreckRockstarEntry{" +
                "voteCount=" + voteCount +
                ", type=" + type +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}