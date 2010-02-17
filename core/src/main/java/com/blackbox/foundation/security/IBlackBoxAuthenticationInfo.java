/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IBlackBoxAuthenticationInfo extends AuthenticationInfo, Serializable {
    void setPrincipals(PrincipalCollection principals);

    void setCredentials(String credentials);
}
