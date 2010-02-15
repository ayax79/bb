package com.blackbox.presentation.action.activity;

import com.blackbox.activity.IActivity;
import com.blackbox.activity.IActivityManager;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import com.blackbox.presentation.action.util.JSONUtil;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJsonArray;
import static com.google.common.collect.Lists.newArrayList;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;

/**
 *
 */
public class LoadChildrenActivityActionBean extends BaseBlackBoxActionBean {

    @SpringBean(value = "activityManager")
    protected IActivityManager activityManager;

    @Validate(required = true)
    private String parentGuid;
    private Collection<IActivity> childrenActivities = newArrayList();

    public Resolution loadComments() throws JSONException {
        childrenActivities = activityManager.loadChildrenActivityByParent(parentGuid);
        if (getView() == json) {
            return createResolutionWithJsonArray(getContext(), threadsToJson(childrenActivities));
        }
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/invitation.jsp");
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public Collection<IActivity> getChildrenActivities() {
        return childrenActivities;
    }

    public void setChildrenActivities(Collection<IActivity> childrenActivities) {
        this.childrenActivities = childrenActivities;
    }

    protected JSONArray threadsToJson(Collection<IActivity> threads) throws JSONException {

        JSONArray array = new JSONArray();
        for (IActivity activity : threads) {
            array.put(JSONUtil.toJSON(activity));
        }
        return array;
    }

}