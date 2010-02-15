package com.blackbox.activity;

import com.blackbox.BBPersistentObject;
import com.blackbox.EntityReference;

public class TrainRockComment extends BBPersistentObject {


    private EntityReference target;
    private IActivity targetObject;
    private Boolean userVote;


    public EntityReference getTarget() {
        return target;
    }

    public void setTarget(EntityReference target) {
        this.target = target;
    }

    public Boolean isUserVote() {
        return userVote;
    }

    public void setUserVote(Boolean userVote) {
        this.userVote = userVote;
    }

    public IActivity getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(IActivity targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TrainRockComment that = (TrainRockComment) o;

        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        if (targetObject != null ? !targetObject.equals(that.targetObject) : that.targetObject != null) return false;
        if (userVote != null ? !userVote.equals(that.userVote) : that.userVote != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (targetObject != null ? targetObject.hashCode() : 0);
        result = 31 * result + (userVote != null ? userVote.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrainRockComment{" +
                "target=" + target +
                ", targetObject=" + targetObject +
                ", userVote=" + userVote +
                '}';
    }
}
