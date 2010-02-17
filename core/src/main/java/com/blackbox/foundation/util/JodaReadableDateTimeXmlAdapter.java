package com.blackbox.foundation.util;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author A.J. Wright
 */
@XmlTransient
public class JodaReadableDateTimeXmlAdapter extends XmlAdapter<Date, ReadableDateTime> {

    @Override
    public ReadableDateTime unmarshal(Date date) throws Exception {
        return new DateTime(date.getTime());
    }

    @Override
    public Date marshal(ReadableDateTime dateTime) throws Exception {
        return new Date(dateTime.getMillis());
    }

}