/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;
import org.apache.shiro.subject.PrincipalCollection;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@RunWith(JMock.class)
public class SecurityUtilUnitTest {

    Mockery context = new JUnit4Mockery();

    @Test
    public void testGetUserFromPrincipal() {
        final PrincipalCollection principalCollection = context.mock(PrincipalCollection.class, "principal");
        final Set set = context.mock(Set.class, "premSet");
        final IUser user = new User();
        context.checking(new Expectations() {
            {
                oneOf(principalCollection).asSet();
                will(returnValue(set));
                oneOf(set).iterator();
                will(returnIterator(user));

            }
        });
        final IUser resultUser = SecurityUtil.getUser(principalCollection);
        assertEquals(user, resultUser);
    }

    @Test
    public void testGetUserBlackBoxAuthenticationInfo() {
        final IBlackBoxAuthenticationInfo blackBoxAuthenticationInfo = context.mock(IBlackBoxAuthenticationInfo.class, "bbAuthentication");
        final PrincipalCollection principalCollection = context.mock(PrincipalCollection.class, "principal");
        final Set set = context.mock(Set.class, "premSet");
        final IUser user = new User();
        context.checking(new Expectations() {
            {
                oneOf(blackBoxAuthenticationInfo).getPrincipals();
                will(returnValue(principalCollection));
                oneOf(principalCollection).asSet();
                will(returnValue(set));
                oneOf(set).iterator();
                will(returnIterator(user));

            }
        });
        final IUser resultUser = SecurityUtil.getUser(blackBoxAuthenticationInfo);
        assertEquals(user, resultUser);
    }

    @Test
    public void testConvertPermissionToWildcardStringFromSocialPermission() {
        SocialPermission perm = new SocialPermission();
        perm.setArtifactType("activity");
        perm.setDepth(1);
        perm.setRelationshipType("fof");
        perm.setTrustLevel(0.5);
        perm.setType(PermissionTypeEnum.SOCIAL);
        final String result = SecurityUtil.convertPermissionToWildcardString(perm);
        assertEquals("fof:1:fof", result);
    }

    @Test
    public void testConvertPermissionToWildcardStringFromAdministrativePermission() {
        ApplicationPermission perm = SecurityFactory.createPermission(PermissionTypeEnum.APPLICATION);
        perm.setArtifactType("activity");
        perm.setAccess("122");
        perm.setCategory("cat1");
        perm.setField("f1");
        perm.setType(PermissionTypeEnum.APPLICATION);
        final String result = SecurityUtil.convertPermissionToWildcardString(perm);
        assertEquals("cat1:122:f1", result);
    }

    @Test
    public void testConvertPermissionToWildcardStringFromSocialBBPermission() {
        SocialPermission perm = new SocialPermission();
        perm.setArtifactType("activity");
        perm.setDepth(1);
        perm.setRelationshipType("fof");
        perm.setTrustLevel(0.5);
        perm.setType(PermissionTypeEnum.SOCIAL);
        final String result = SecurityUtil.convertPermissionToWildcardString((BaseBBPermission) perm);
        assertEquals("fof:1:fof", result);
    }

    @Test
    public void testConvertPermissionToWildcardStringFromAdministrativeBBPermission() {
        ApplicationPermission perm = SecurityFactory.createPermission(PermissionTypeEnum.APPLICATION);
        perm.setArtifactType("activity");
        perm.setAccess("122");
        perm.setCategory("cat1");
        perm.setField("f1");
        perm.setType(PermissionTypeEnum.APPLICATION);
        final String result = SecurityUtil.convertPermissionToWildcardString((BaseBBPermission) perm);
        assertEquals("cat1:122:f1", result);
    }


}
