package com.blackbox.presentation.extension;

import net.sourceforge.stripes.format.Formatter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * @author A.J. Wright
 */
@SuppressWarnings({"JavaDoc"})
public class JodaDateTimeFormatter implements Formatter<DateTime> {


    private String formatType;
    private String formatPattern;
    private Locale locale;
    private DateTimeFormatter format;

    /** Sets the format type to be used to render dates as Strings. */
    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    /** Gets the format type to be used to render dates as Strings. */
    public String getFormatType() {
        return formatType;
    }

    /** Sets the named format string or date pattern to use to format the date. */
    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    /** Gets the named format string or date pattern to use to format the date. */
    public String getFormatPattern() {
        return formatPattern;
    }

    /** Sets the locale that output String should be in. */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /** Gets the locale that output String should be in. */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Constructs the DateFormat used for formatting, based on the values passed to the
     * various setter methods on the class.  If the formatString is one of the named formats
     * then a DateFormat instance is created of the specified type and format, otherwise
     * a SimpleDateFormat is constructed using the pattern provided and the formatType is ignored.
     *
     * @throws net.sourceforge.stripes.exception.StripesRuntimeException if the formatType is not one of 'date', 'time' or 'datetime'.
     */
    public void init() {
        // Default these values if they were not supplied
        if (formatPattern == null) {
            formatPattern = "short";
        }

        if (formatType == null) {
            formatType = "date";
        }

        String lcFormatString = formatPattern.toLowerCase();
        String lcFormatType  = formatType.toLowerCase();

        // Now figure out how to construct our date format for our locale
        if (lcFormatType.equals("date") && lcFormatString.equals("short")) {
            format = DateTimeFormat.shortDate().withLocale(locale);
        }
        else if (lcFormatType.equals("date") && lcFormatString.equals("medium")) {
            format = DateTimeFormat.mediumDate().withLocale(locale);
        }
        else if (lcFormatType.equals("date") && lcFormatString.equals("long")) {
            format = DateTimeFormat.longDate().withLocale(locale);
        }
        else if (lcFormatType.equals("date") && lcFormatString.equals("full")) {
            format = DateTimeFormat.fullDate().withLocale(locale);
        }
        else if (lcFormatType.equals("datetime") && lcFormatString.equals("short")) {
            format = DateTimeFormat.shortDateTime().withLocale(locale);
        }
        else if (lcFormatType.equals("datetime") && lcFormatString.equals("medium")) {
            format = DateTimeFormat.mediumDateTime().withLocale(locale);
        }
        else if (lcFormatType.equals("datetime") && lcFormatString.equals("long")) {
            format = DateTimeFormat.longDateTime().withLocale(locale);
        }
        else if (lcFormatType.equals("datetime") && lcFormatString.equals("full")) {
            format = DateTimeFormat.fullDateTime().withLocale(locale);
        }
        else {
            format = DateTimeFormat.forPattern(formatPattern).withLocale(locale);
        }
    }

    /** Formats a Date as a String using the rules supplied when the formatter was built. */
    public String format(DateTime input) {
        return input.toString(this.format);
    }
}
