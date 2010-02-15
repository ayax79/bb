package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;
import com.blackbox.Utils;

import java.io.IOException;

/**
 * @author A.J. Wright
 */
public class UtilActionBean implements ActionBean {

    private ActionBeanContext context;

    private String echo;
    private String url;

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @DefaultHandler
    public Resolution echo() {
        return createResolutionWithText(context, echo);
    }

    public Resolution checkUrl() throws IOException {
        int result = Utils.checkUrl(url);
        return createResolutionWithText(context, String.valueOf(result));
    }
}
