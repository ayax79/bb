package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.user.ExternalCredentials;

/**
 * @author A.J. Wright
 */
public class ExternalCredentialTypeHandler extends OrdinalEnumTypeHandler {

    public ExternalCredentialTypeHandler() {
        super(ExternalCredentials.CredentialType.class);
    }

}
