/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.commerce.event;

import com.blackbox.social.Category;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

@EventResultType(List.class)
public class LoadProductsByCategoriesEvent extends BaseEvent<List<Category>> {
    private List<Category> categories;
    private static final long serialVersionUID = -2892813301233747141L;

    public LoadProductsByCategoriesEvent(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}