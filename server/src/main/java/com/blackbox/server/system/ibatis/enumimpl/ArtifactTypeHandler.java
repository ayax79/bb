package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import org.yestech.publish.objectmodel.ArtifactType;

/**
 * @author A.J. Wright
 */
public class ArtifactTypeHandler extends OrdinalEnumTypeHandler {

    public ArtifactTypeHandler() {
        super(ArtifactType.class);
    }
}
