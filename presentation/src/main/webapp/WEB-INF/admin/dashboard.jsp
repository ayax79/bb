<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<s:messages/>
<s:errors/>
<h2>Select an Operation</h2>

<div class="clearfix"></div>
<ol>
    <li>
        <s:link beanclass="com.blackbox.presentation.action.admin.SystemAdminActionBean" event="findUser">
            User administration<span>Update user info, user type, active status, etc.</span>
        </s:link>
    </li>
    <li>Promo code administration<span>View, Edit, and Create new promo codes</span>
        <ul>
            <%--<li><s:link beanclass="com.blackbox.presentation.action.admin.PromoCodeAdminActionBean"
                        event="list">View All</s:link></li>--%>
            <li><s:link beanclass="com.blackbox.presentation.action.admin.PromoCodeAdminActionBean">Create a new promo code</s:link></li>
        </ul>
    </li>
</ol>