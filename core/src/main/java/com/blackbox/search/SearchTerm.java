/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.search;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public enum SearchTerm implements ISearchTerms {
    STATUS("status"), IDENTIFIER("identifier"), CATEGORIES_NAME("categories.name"),
    CATEGORIES_IDENTIFIER("categories.identifier"),
    TAGS_NAME("tags.name"),
    TAGS_IDENTIFIER("tags.identifier"),
    SUPPLIER_IDENTIFIER("supplier.identifier"), SUPPLIER_NAME("supplier.name");

    private String fieldName;

    SearchTerm(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
