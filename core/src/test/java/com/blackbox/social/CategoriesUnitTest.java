/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.social;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.yestech.lib.xml.XmlUtils.fromXmlJaxb;
import static org.yestech.lib.xml.XmlUtils.toXmlJaxb;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class CategoriesUnitTest {

    @Test
    public void testJAXBMarshalForEmptyList() {
        Categories categories = new Categories();
        String xml = toXmlJaxb(categories);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><categories/>", xml);
    }

    @Test
    public void testJAXBMarshalForListOfOne() {
        List<Category> list = newArrayList();
        Category cat1 = new Category();
        cat1.setName("cat1");
        list.add(cat1);
        Categories categories = new Categories();
        categories.setCategoryCollection(list);
        String xml = toXmlJaxb(categories);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><categories><category><name>cat1</name></category></categories>", xml);
    }

    @Test
    public void testJAXBMarshalForList() {
        List<Category> list = newArrayList();
        Category cat1 = new Category();
        cat1.setName("cat1");
        list.add(cat1);
        Category cat2 = new Category();
        cat2.setName("cat2");
        list.add(cat2);
        Categories categories = new Categories();
        categories.setCategoryCollection(list);
        String xml = toXmlJaxb(categories);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><categories><category><name>cat1</name></category><category><name>cat2</name></category></categories>", xml);
    }

    @Test
    public void testJAXBUnMarshalForList() {
        List<Category> list = newArrayList();
        Category cat1 = new Category();
        cat1.setName("cat1");
        list.add(cat1);
        Category cat2 = new Category();
        cat2.setName("cat2");
        list.add(cat2);
        Categories categories = new Categories();
        categories.setCategoryCollection(list);
        Categories resultCategories = fromXmlJaxb(categories, "<?xml version=\"1.0\" ?><categories><category><name>cat1</name></category><category><name>cat2</name></category></categories>");
        assertEquals(cat1, resultCategories.getCategoryCollection().get(0));
        assertEquals(cat2, resultCategories.getCategoryCollection().get(1));
        assertEquals(categories, resultCategories);
    }
}
