package com.blackbox.server.bookmark.event;

import com.blackbox.bookmark.Bookmark;
import org.yestech.event.event.BaseEvent;

/**
 * @author A.J. Wright
 */
public class AbstractBookmarkChangeEvent extends BaseEvent<Bookmark> {
    private static final long serialVersionUID = 4446834128423839400L;

    public AbstractBookmarkChangeEvent(Bookmark bookmark) {
        super(bookmark);
    }
}
