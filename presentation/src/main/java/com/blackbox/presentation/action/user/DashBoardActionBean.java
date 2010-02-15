/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
*
* Original Author:  Artie Copeland
* Last Modified Date: $DateTime: $
*/
@UrlBinding("/dashboard")
public class DashBoardActionBean extends BaseBlackBoxActionBean
{
    @DontValidate
    public Resolution begin()
    {
        return new ForwardResolution("/dashboard.jsp");
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

}