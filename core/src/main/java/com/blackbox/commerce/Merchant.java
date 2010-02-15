/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import com.blackbox.EntityTypeAnnotation;
import com.blackbox.Utils;
import com.blackbox.Vendor;
import com.blackbox.social.Category;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.yestech.lib.xml.XmlUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.blackbox.EntityType.MERCHANT;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("merchant")
@EntityTypeAnnotation(MERCHANT)
@XmlRootElement(name = "merchant")
public class Merchant extends Vendor {
    private List<Category> categories;
    private String integrationGuid;

    public Merchant() {
        super(MERCHANT);
    }

    public String getIntegrationGuid() {
        return integrationGuid;
    }

    public void setIntegrationGuid(String integrationGuid) {
        this.integrationGuid = integrationGuid;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Used to create a Merchant from an xml
     *
     * @param xml
     * @return
     */
    public static Merchant valueOf(String xml) {
        return XmlUtils.fromXml(xml, Utils.xmlAliases());
    }
}
