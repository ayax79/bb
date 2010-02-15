package com.blackbox.presentation.action.feedback;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

public class ReportAbuseActionBean  extends BaseBlackBoxActionBean {

	private String subject;
	private String description;

    @DefaultHandler
    public Resolution begin() {
        return new ForwardResolution("/ajax/feedback/report-abuse.jspf");
    }

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
