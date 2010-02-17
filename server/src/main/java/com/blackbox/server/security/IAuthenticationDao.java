/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.security;

import com.blackbox.foundation.security.IBlackBoxAuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IAuthenticationDao {
    IBlackBoxAuthenticationInfo load(AuthenticationToken authenticationToken);
}
