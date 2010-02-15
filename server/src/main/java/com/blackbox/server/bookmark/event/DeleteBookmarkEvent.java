package com.blackbox.server.bookmark.event;

import com.blackbox.bookmark.Bookmark;

/**
 *
 *
 */
public class DeleteBookmarkEvent extends AbstractBookmarkChangeEvent {
    private static final long serialVersionUID = 6381274022507500329L;

    public DeleteBookmarkEvent(Bookmark bookmark) {
        super(bookmark);
    }
}
