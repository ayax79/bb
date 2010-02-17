package com.blackbox.foundation.util;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author A.J. Wright
 */
@XmlTransient
public class JodaDateTimeXmlAdapter extends XmlAdapter<Date, DateTime> {

    @Override
    public DateTime unmarshal(Date date) throws Exception {
        return new DateTime(date.getTime());
    }

    @Override
    public Date marshal(DateTime dateTime) throws Exception {
        return new Date(dateTime.getMillis());
    }

}
