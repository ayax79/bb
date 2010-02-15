package com.blackbox.presentation.action.util;

import com.blackbox.social.Address;
import com.blackbox.billing.BillingInfo;
import net.sourceforge.stripes.action.ActionBeanContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

public final class EpxUtil {

    private static final DateTimeFormatter AUTH_TRAN_DATE_FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss a");
    private static final DateTimeFormatter EXP_DATE_FORMATTER = DateTimeFormat.forPattern("yyMM");


    private EpxUtil() {
    }

    public static BillingInfo buildEpxBillingInfo(ActionBeanContext context) {
        BillingInfo billing = new BillingInfo();
        Address address = new Address();
        billing.setAddress(address);

        HttpServletRequest request = context.getRequest();

        billing.setProviderGuid(request.getParameter("AUTH_GUID"));
        billing.setBillingPhone(request.getParameter("PHONE_HM"));

        address.setZipCode(request.getParameter("ZIP_CODE"));
        address.setCity(request.getParameter("CITY"));
        address.setAddress1(request.getParameter("ADDRESS"));
        address.setState(request.getParameter("STATE"));

        billing.setLastExpirationDate(parseExpDate(request.getParameter("EXP_DATE")));   // 1001
        billing.setLastBilled(parseAuthTranDate(request.getParameter("AUTH_TRAN_DATE_GMT"))); // AUTH_TRAN_DATE_GMT=[11/30/2009 10:34:29 PM]
        billing.setLastResponse(request.getParameter("AUTH_RESP_TEXT")); // AUTH_RESP_TEXT=[APPROVAL 004541 ]
        billing.setAuthResponse(request.getParameter("AUTH_RESP")); // AUTH_RESP=[00]
        billing.setLastAmount(request.getParameter("AMOUNT"));  // AMOUNT=[25.00]
        billing.setLastCardNum(request.getParameter("AUTH_ACCOUNT_NBR"));//AUTH_ACCOUNT_NBR=[1111]
        billing.setLastCardType(request.getParameter("AUTH_CARD_TYPE"));//AUTH_CARD_TYPE=[V]
        billing.setFirstName(request.getParameter("FIRST_NAME"));
        billing.setLastName(request.getParameter("LAST_NAME"));

        return billing;
    }

    static DateTime parseExpDate(String date) {
        if (date == null) return null;
        return EXP_DATE_FORMATTER.parseDateTime(date);
    }

    static DateTime parseAuthTranDate(String date) {
        if (date == null) return null;

        return AUTH_TRAN_DATE_FORMATTER.parseDateTime(date);
    }

}
