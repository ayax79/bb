package com.blackbox.presentation.action.admin;

import net.sourceforge.stripes.action.UrlBinding;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BB platform reporting functions
 *
 * @author Andy Nelsen
 * @date Nov 30, 2009
 */
@UrlBinding("/reporting/{$event}")
public class ReportingActionBean extends BaseBlackBoxActionBean {
    private static final Logger _logger = LoggerFactory.getLogger(ReportingActionBean.class);
}
