/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;


import com.blackbox.foundation.user.User;
import org.apache.shiro.subject.PrincipalCollection;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class BlackBoxAuthenticationInfo implements IBlackBoxAuthenticationInfo {

    @XmlTransient
    private PrincipalCollection principals;
    private String credentials;
    private static final long serialVersionUID = -1763986960780241570L;

    public void setPrincipals(PrincipalCollection principals) {
        this.principals = principals;
    }

    @XmlTransient
    @Override
    public PrincipalCollection getPrincipals() {
        return principals;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }
//
//    public JAXBSafeAuthenticationInfo toJaxbSafeAuthenticationInfo() {
//        if (principals == null) return null;
//
//        ArrayList<JAXBSafeAuthenticationInfo.PrincipalReference> list = new ArrayList<JAXBSafeAuthenticationInfo.PrincipalReference>();
//
//        for (String realmName : principals.getRealmNames()) {
//            for (Object o : principals.fromRealm(realmName)) {
//                JAXBSafeAuthenticationInfo.PrincipalReference ref = new JAXBSafeAuthenticationInfo.PrincipalReference(realmName, (User) o);
//                list.add(ref);
//            }
//
//        }
//
//        JAXBSafeAuthenticationInfo info = new JAXBSafeAuthenticationInfo();
//        info.setCredentials(credentials);
//        info.setPrincipals(list);
//        return info;
//    }
}
