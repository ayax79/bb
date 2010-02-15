<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<script type="text/javascript">
    //<![CDATA[
    $(function() {
        var startYear = (new Date().getFullYear());
        var endYear = (new Date().getFullYear() +2);
        $('#expirationDate').datepicker({
            showOn: 'button',
            buttonImage: '${bb:libraryResource('/library/images/nu-CalendarButton.png')}',
            buttonImageOnly: true,
            changeMonth: true,
            changeYear: true,
            dateFormat:'mm/dd/yy',
            yearRange: startYear + ':' +endYear
        });
    })
</script>
<h2>Promo code generator</h2>

<div class="clearfix"></div>
<s:messages/>
<s:errors/>

<s:form beanclass="com.blackbox.presentation.action.admin.PromoCodeAdminActionBean" method="post">

    <fieldset>
        <legend>Create a Single Use Promo Code</legend>
        <div style="float: right;">
            <c:if test="${actionBean.promoCode != null}">
                <div class="admin-message">Successfully created promo code:
                    <strong>${actionBean.promoCode.code}</strong></div>
            </c:if>
        </div>
        <table>
            <tr>
                <td><s:label for="username">Username:</s:label></td>
                <td><s:text name="username"/></td>
            </tr>
            <tr>
                <td><s:label for="email">Email:</s:label></td>
                <td><s:text name="email"/></td>
            </tr>
            <tr>
                <td><s:label for="userType">User type:</s:label></td>
                <td><s:select name="userType">
                    <s:options-enumeration enum="com.blackbox.user.User.UserType"/>
                </s:select></td>
            </tr>
            <tr>
                <td><s:label for="evaluationPeriod">Evaluation Period:</s:label></td>
                <td><s:select name="evaluationPeriod">
                    <s:option value="1">1 month</s:option>
                    <s:option value="1">3 months</s:option>
                    <s:option value="1">6 months</s:option>
                    <s:option value="1">12 months</s:option>
                </s:select></td>
            </tr>
        </table>
        <s:submit name="singleUse" style="display: block;">Generate</s:submit>
    </fieldset>
</s:form>
<s:form beanclass="com.blackbox.presentation.action.admin.PromoCodeAdminActionBean" method="post">

    <fieldset>
        <legend>Create a Multiple Use Promo Campaign Code</legend>
        <div style="float: right;">
            <c:if test="${actionBean.promoCodeMulti != null}">
                <div class="admin-message">Successfully created promo code:
                    <strong>${actionBean.promoCodeMulti.code}</strong></div>
            </c:if>
        </div>
        <table>
            <tr>
                <td><s:label for="promoCampaignName">Campaign name:</s:label></td>
                <td><s:text name="promoCampaignName"/></td>
            </tr>
            <tr>
                <td><s:label for="username">Username:</s:label></td>
                <td><s:text name="username"/></td>
            </tr>
            <tr>
                <td><s:label for="usertype">User type:</s:label></td>
                <td><s:select name="userType">
                    <s:options-enumeration enum="com.blackbox.user.User.UserType"/>
                </s:select></td>
            </tr>
            <tr>
                <td><s:label for="evaluationPeriod">Evaluation Period:</s:label></td>
                <td><s:select name="evaluationPeriod">
                    <s:option value="1">1 month</s:option>
                    <s:option value="1">3 months</s:option>
                    <s:option value="1">6 months</s:option>
                    <s:option value="1">12 months</s:option>
                </s:select></td>
            </tr>
            <tr>
                <td>
                    <s:label for="expirationDate">Invite expires:</s:label>
                </td>
                <td>
                    <s:text id="expirationDate" class="textinput" name="expirationDate" value="MM/DD/YYYY"
                            onfocus="if(this.value=='MM/DD/YYYY'){this.value=''}"/>
                </td>
            </tr>
        </table>
        <s:submit name="multiUse" style="display: block;">Generate</s:submit>
    </fieldset>
</s:form>
