package com.blackbox.presentation.action.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.joda.time.DateTime;
import com.blackbox.presentation.extension.MockBlackBoxContext;
import static com.blackbox.foundation.social.Relationship.createRelationship;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.bookmark.Bookmark;
import static com.blackbox.foundation.bookmark.Bookmark.BookmarkType.WISH;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author A.J. Wright
 */
public class JspFunctionsUnitTest {

    @Test
    public void testFormatDate() {
        DateTime dt = new DateTime();
        String string = JspFunctions.formatDate(dt, "MM/dd/yyyy");
        assertTrue(string.matches("[0-9]{2}\\/[0-9]{2}\\/(1|2)(0|9)[0-9][0-9]"));
    }

    @Test
    public void yearsDifferenceTest() {
        DateTime birth = new DateTime(1981, 11, 2, 3, 55, 22, 22);
        DateTime current = new DateTime(2009, 8, 1, 0, 0, 0, 0);
        int result = JspFunctions.yearsDifference(birth, current);
        assertEquals(27, result);
    }

    @Test
    public void ageTest() {
        DateTime birth = new DateTime().minusYears(10);
        int result = JspFunctions.age(birth);
        assertEquals(10, result);
    }


    @Test
    public void isMutualWish() {
        User current = User.createUser();
        User other = User.createUser();

        Bookmark bookmark0 = Bookmark.createBookmark();
        bookmark0.setType(WISH);
        bookmark0.setTarget(other.toEntityReference());

        Bookmark bookmark1 = Bookmark.createBookmark();
        bookmark1.setType(WISH);
        bookmark1.setOwner(other);
        bookmark1.setTarget(current.toEntityReference());

        MockBlackBoxContext context = new MockBlackBoxContext();
        context.setBookmarks(newArrayList(bookmark0, bookmark1));
        PresentationResourceHolder.setContext(context);

    }

    @Test
    public void testReplaceDonkey() {
        String body = "sadfasdflkj sdfajsldef adslfkjslefd asdlkfj{DONKEY} A;LSDKFJSADF ";
        String result = JspFunctions.replaceDonkey(body, "poop");
        assertTrue(result.contains("poop"));
    }


}
