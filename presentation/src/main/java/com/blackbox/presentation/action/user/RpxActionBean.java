package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import org.json.JSONException;

/**
 * @author Matthew Sweet
 */

public class RpxActionBean extends BaseBlackBoxActionBean {

	private String token;


	@DefaultHandler
	@DontValidate
	public Resolution transactionInfo() throws JSONException {

		

		return new ForwardResolution("/poptest.jsp");


	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
