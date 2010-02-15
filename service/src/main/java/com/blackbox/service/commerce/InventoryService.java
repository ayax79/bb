/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.commerce;

import com.blackbox.Utils;
import com.blackbox.commerce.IInventoryManager;
import com.blackbox.commerce.Inventory;
import com.blackbox.commerce.Product;
import com.blackbox.service.security.AuthenticationUtils;
import com.blackbox.social.Categories;
import com.blackbox.social.Category;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.lib.xml.XmlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Service
public class InventoryService implements IInventoryService {
    final private static Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Resource(name = "serviceAuthentication")
    private AuthenticationUtils authentication;

    @Resource(name = "inventoryManager")
    private IInventoryManager inventoryManager;

    public void setAuthentication(AuthenticationUtils authentication) {
        this.authentication = authentication;
    }

    public void setInventoryManager(IInventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @Override
    public List<Product> loadProductsByCategories(@PathParam("session") String session,
                                                  @PathParam("categories") Categories categories) {

        List<Category> categoryList = categories.getCategoryCollection();
        return inventoryManager.loadProductsByCategories(categoryList);
    }

    @Override
    public String uploadInventory(@PathParam("merchantGuid") String merchantGuid, String inventoryXml) {
        String processingResult = "FAILED";

        if (authentication.isValidateMerchant(merchantGuid) && StringUtils.isNotBlank(processingResult)) {
            try {
                Inventory inventory = XmlUtils.fromXml(inventoryXml, Utils.xmlAliases(), true);
                inventoryManager.saveInventory(inventory);
                processingResult = "SUCCESS";
            } catch (Throwable t) {
                logger.error("error processing inventory upload: " + inventoryXml, t);
                processingResult = processingResult + "error processing inventory upload: " + inventoryXml + " ::  " + t.getMessage();
            }
        }
        return processingResult;
    }

    @Override
    public String deleteInventory(@PathParam("merchantGuid") String merchantGuid, String inventoryXml) {
        String processingResult = "FAILED";

        if (authentication.isValidateMerchant(merchantGuid) && StringUtils.isNotBlank(processingResult)) {
            try {
                Inventory inventory = XmlUtils.fromXml(inventoryXml, Utils.xmlAliases(), true);
                inventoryManager.removeInventory(inventory);
                processingResult = "SUCCESS";
            } catch (Throwable t) {
                logger.error("error processing inventory delete: " + inventoryXml, t);
                processingResult = processingResult + "error processing inventory upload: " + inventoryXml + " ::  " + t.getMessage();
            }
        }
        return processingResult;
    }

    @Override
    public String uploadProduct(@PathParam("merchantGuid") String merchantGuid, String productXml) {
        String processingResult = "FAILED";

        if (authentication.isValidateMerchant(merchantGuid) && StringUtils.isNotBlank(processingResult)) {
            try {
                Product product = XmlUtils.fromXml(productXml, Utils.xmlAliases(), true);
                inventoryManager.saveProduct(product);
                processingResult = "SUCCESS";
            } catch (Throwable t) {
                logger.error("error processing product upload: " + productXml, t);
                processingResult = processingResult + "error processing inventory upload: " + productXml + " ::  " + t.getMessage();
            }
        }
        return processingResult;
    }

    @Override
    public String loadInventory() {
        return XmlUtils.toXml(inventoryManager.loadInventory(), Utils.xmlAliases(), true);
    }
}
