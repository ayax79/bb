<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<s:form beanclass="com.blackbox.presentation.action.admin.SystemAdminActionBean" method="post">
    <s:hidden name="user.guid"/>

    <h2>${actionBean.user.username}</h2>
    <div class="clearfix"></div>
    <s:errors/>
    <s:messages/>
    <table border="0" cellpadding="0" cellspacing="0" class="admin">
        <thead>
        <tr>
            <th scope="col">Field</th>
            <th scope="col" colspan="2">Current Value (Change to update)</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row"><s:label for="user.username">Username</s:label></th>
            <td><s:text name="user.username"/></td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.password">New Password</s:label></th>
            <td><s:password name="user.password"/></td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.name">First Name</s:label></th>
            <td><s:text name="user.name"/></td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.lastname">Last Name</s:label></th>
            <td><s:text name="user.lastname"/></td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.type">Usertype</s:label></th>
            <td>
                <s:select name="user.type">
                    <s:options-enumeration
                            enum="com.blackbox.user.User.UserType"></s:options-enumeration>
                </s:select>
            </td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.status">Status</s:label></th>
            <td>
                <s:select name="user.status">
                    <s:options-enumeration
                            enum="com.blackbox.Status"></s:options-enumeration>
                </s:select>
            </td>
        </tr>
        <tr>
            <th scope="row"><s:label for="user.email">Email</s:label></th>
            <td><s:text name="user.email"/></td>
        </tr>
        </tbody>
    </table>
    <s:submit name="update_user">Update</s:submit>
    <s:field-metadata var="fieldMetadata">
        $(function(){
        $.fn.stripesValidation('${fieldMetadata.formId}', ${fieldMetadata});
        });
    </s:field-metadata>
</s:form>
                