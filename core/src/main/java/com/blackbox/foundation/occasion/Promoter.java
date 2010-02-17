/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.occasion;

import com.blackbox.foundation.EntityTypeAnnotation;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.Vendor;

import javax.xml.bind.annotation.XmlRootElement;

import static com.blackbox.foundation.EntityType.PROMOTER;

/**
 * @author Artie Copeland
 * @version $Revision: $
 *          THIS CLASS IS NOT USED, USE user.getEntityType to determine if a user is a promoter
 */
@EntityTypeAnnotation(PROMOTER)
@XmlRootElement(name = "promoter")
public class Promoter extends Vendor {

    public Promoter() {
        super(PROMOTER);
    }

    public static Promoter createPromoter() {
        Promoter p = new Promoter();
        Utils.applyGuid(p);
        return p;
    }
}
