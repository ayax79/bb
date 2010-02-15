package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.bookmark.Bookmark;

/**
 * @author A.J. Wright
 */
public class BookmarkTypeHandler extends OrdinalEnumTypeHandler {

    public BookmarkTypeHandler() {
        super(Bookmark.BookmarkType.class);
    }
}
