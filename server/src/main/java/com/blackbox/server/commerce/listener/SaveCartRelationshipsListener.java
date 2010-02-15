/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.commerce.CartItem;
import com.blackbox.commerce.Invoice;
import com.blackbox.commerce.ShoppingCart;
import com.blackbox.commerce.Split;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.event.PurchaseCartEvent;
import com.blackbox.server.social.INetworkDao;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;
import static com.google.common.collect.Lists.newArrayList;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(PurchaseCartEvent.class)
@AsyncListener
public class SaveCartRelationshipsListener extends BaseBlackboxListener<PurchaseCartEvent, Invoice> {
    private INetworkDao networkDao;
    private int weight = Relationship.RelationStatus.FRIEND.getWeight();

    @Override
    public void handle(PurchaseCartEvent purchaseCartEvent, ResultReference<Invoice> result) {
        Invoice invoice = purchaseCartEvent.getType();
        ShoppingCart shoppingCart = invoice.getCart();
        Collection<Split> splits = invoice.getSplits();
        associateUserToProduct(shoppingCart, splits);
        associateUserToUser(splits);
        associateProductToProduct(shoppingCart.getItems());
        associateProductToUser(shoppingCart, splits);
    }

    private void associateProductToProduct(Map<String, CartItem> items) {
        Collection<CartItem> itemClone = newArrayList(items.values());
        for (CartItem cartItem : items.values()) {
            for (CartItem item : itemClone) {
                if (!cartItem.equals(item)) {
                    Relationship relationship = Relationship.createRelationship(cartItem.getProduct(), item.getProduct(), weight);
                    networkDao.save(relationship);
                }
            }
        }
    }

    private void associateUserToUser(Collection<Split> splits) {
        Collection<Split> splitClone = newArrayList(splits);
        for (Split fromSplit : splits) {
            for (Split toSplit : splitClone) {
                if (!fromSplit.equals(toSplit)) {
                    Relationship relationship = Relationship.createRelationship(fromSplit.getFirst(), toSplit.getFirst(), weight);
                    networkDao.save(relationship);
                }
            }
        }
    }

    private void associateUserToProduct(ShoppingCart shoppingCart, Collection<Split> splits) {
        for (Split split : splits) {
            for (CartItem cartItem : shoppingCart.getItems().values()) {
                User user = split.getFirst();
                Relationship relationship = Relationship.createRelationship(user, cartItem.getProduct(), weight);
                networkDao.save(relationship);
            }
        }
    }

    private void associateProductToUser(ShoppingCart shoppingCart, Collection<Split> splits) {
        for (CartItem cartItem : shoppingCart.getItems().values()) {
            for (Split split : splits) {
                User user = split.getFirst();
                Relationship relationship = Relationship.createRelationship(cartItem.getProduct(), user, weight);
                networkDao.save(relationship);
            }
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Resource(name = "networkDao")
    public void setNetworkDao(INetworkDao networkDao) {
        this.networkDao = networkDao;
    }
}
