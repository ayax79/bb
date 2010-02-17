package com.blackbox.foundation.util;

import org.joda.time.DateMidnight;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author A.J. Wright
 */
@XmlTransient
public class JodaDateMidnightXmlAdapter extends XmlAdapter<Date, DateMidnight> {

    @Override
    public DateMidnight unmarshal(Date date) throws Exception {
        return new DateMidnight(date.getTime());
    }

    @Override
    public Date marshal(DateMidnight dateTime) throws Exception {
        return new Date(dateTime.getMillis());
    }

}