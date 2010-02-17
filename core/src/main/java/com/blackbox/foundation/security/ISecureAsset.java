package com.blackbox.foundation.security;

/**
 * Resents any type of assest that need to have security wrapped on it.
 */
public interface ISecureAsset extends IAsset {
    public AccessControlList getAccessControlList();

    public void setAccessControlList(AccessControlList accessControlList);
}