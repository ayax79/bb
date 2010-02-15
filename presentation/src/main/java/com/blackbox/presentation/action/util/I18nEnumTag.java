package com.blackbox.presentation.action.util;

import com.blackbox.Utils;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.tag.StripesTagSupport;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author A.J. Wright
 */
public class I18nEnumTag extends StripesTagSupport {


    private Enum value;

    public Enum getValue() {
        return value;
    }

    public void setValue(Enum value) {
        this.value = value;
    }

    /**
     * Does nothing, all processing is performed in doEndTag().
     *
     * @return SKIP_BODY in all cases.
     */
    @Override
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }


    @Override
    public int doEndTag() throws JspException {
        try {

            if (value != null) {
                JspWriter writer = getPageContext().getOut();

                // Output all errors in a standard format
                Locale locale = getPageContext().getRequest().getLocale();
                ResourceBundle bundle = StripesFilter.getConfiguration()
                        .getLocalizationBundleFactory().getErrorMessageBundle(locale);

                String string = bundle.getString(value.getClass().getSimpleName() + "." + Utils.enumName(value));
                if (StringUtils.isBlank(string)) {
                    string = bundle.getString(value.getClass().getName() + "." + Utils.enumName(value));
                }

                writer.write(string);
            }

            return EVAL_PAGE;
        }
        catch (IOException e) {
            throw new JspException("IOException encountered while writing messages " +
                    "tag to the JspWriter.", e);
        }
    }
}
