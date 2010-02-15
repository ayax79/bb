package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.media.MediaLibrary;

/**
 * @author A.J. Wright
 */
public class MediaLibraryTypeHandler extends OrdinalEnumTypeHandler {

    public MediaLibraryTypeHandler() {
        super(MediaLibrary.MediaLibraryType.class);
    }
}
