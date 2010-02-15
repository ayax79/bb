/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.occasion;

import com.blackbox.EntityTypeAnnotation;
import com.blackbox.Utils;
import com.blackbox.Vendor;

import javax.xml.bind.annotation.XmlRootElement;

import static com.blackbox.EntityType.PROMOTER;

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
