package com.blackbox.server.bookmark.event;

import com.blackbox.foundation.bookmark.Bookmark;

/**
 * @author A.J. Wright
 */
public class CreateBookmarkEvent extends AbstractBookmarkChangeEvent {
    private static final long serialVersionUID = 1161923179462931203L;

    public CreateBookmarkEvent(Bookmark bookmark) {
        super(bookmark);
    }
}
