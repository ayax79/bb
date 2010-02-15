<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:messages/>
<s:errors/>
<h2>Select an Operation</h2>

<div class="clearfix"></div>
<ol>
    <li>
        <s:link beanclass="com.blackbox.presentation.action.admin.MarketingAdminActionBean" event="findUser">
            User type administration<span>Affiliate promotion and demotion.</span>
        </s:link>
    </li>
    <li>
        <s:link beanclass="com.blackbox.presentation.action.admin.PromoCodeAdminActionBean">
            Promo code administration<span>Create promo codes.</span>
        </s:link>
    </li>
    <li>
        <s:link beanclass="com.blackbox.presentation.action.admin.CampaignCodeAdminActionBean">
            Campaign code administration<span>Create campaigns and campaign users.</span>
        </s:link>
    </li>
</ol>