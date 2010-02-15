package com.blackbox.presentation.action.social;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.JSONUtil;
import com.blackbox.social.ISocialManager;
import com.blackbox.social.Ignore;
import com.blackbox.user.IUser;
import com.blackbox.EntityReference;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.StringReader;
import java.util.List;

/**
 *
 *
 */
public class IgnoreActionBean extends BaseBlackBoxActionBean {

    @SpringBean("socialManager")
    private ISocialManager socialManager;

    private List<EntityReference> ignores;

    @DontValidate
    public Resolution list() throws JSONException {

        IUser currentUser = getCurrentUser();

        return new ForwardResolution("/WEB-INF/jsp/include/ignores.jspf");
    }

    public List<EntityReference> getIgnores() {
        return ignores;
    }

    public void setSocialManager(ISocialManager socialManager) {
        this.socialManager = socialManager;
    }

    protected JSONArray toJSONArray(List<EntityReference> ignores) throws JSONException {
        JSONArray array = new JSONArray();
        for (EntityReference ignore : ignores) {
            JSONUtil.toJSON(ignore);
        }
        return array;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
