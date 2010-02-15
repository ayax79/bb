<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><fmt:message key="pageTitlePrefix" /></title>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/settings.css')}" type="text/css" media="screen, projection" />
	<script type="text/javascript">
	//<![CDATA[

		$(document).ready(function() {
			$('#register_country').change(function() {
				if ($(this).val() == 'UNITED_STATES') {
					$('#us_states').show().focus();
				} else if ($(this).val() == 'CANADA') {
					$('#canada_provinces').show().focus();
				} else if ($(this).val() == 'UNITED_KINGDOM' || $(this).val() == 'IRELAND') {
					$('#county').show().focus();
				} else {
					$(this).val('');
					alert("<fmt:message key="register.country.notsupported" />");
				}
			});
		});
	//]]>
	</script>
</head>
<body>

<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<h1 class="white">Billing Information</h1>
		<div class="widget-top">
			<div class="widget-top-right"></div>
		</div>
		<div class="span-24 main-col settings">
			<div class="settings-content">
				<s:form class="bbsettings" action="billing_information.jsp">
					<ui:roundedBox className="rounded-comment">
						<fieldset>
							<h2>Billing Address</h2>
							<ul>
								<li>
									<label for="register_realName"><fmt:message key="register.field.realName" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_realName" name="user.name" /></span>
								</li>

								<li>
									<label for="register_address1"><fmt:message key="register.field.address" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_address1" name="paymentInfo.address.address1" /></span>
								</li>
								<li>
									<span class="textinput37"><s:text id="register_address2" name="paymentInfo.address.address2" /></span>
								</li>
								<li>
									<label for="register_city"><fmt:message key="register.field.city" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_city" name="paymentInfo.address.city" /></span>
								</li>
								<li>
									<label for="register_country"><fmt:message key="register.field.country" /><em>*</em></label>
									<s:select id="register_country" name="paymentInfo.address.country">
										<s:option><fmt:message key="register.select.country" /> </s:option>
										<s:options-enumeration enum="org.yestech.lib.i18n.CountryEnum" />
									</s:select>
								</li>
								<li id="us_states" style="display:none;">
									<label for="register_state"><fmt:message key="register.field.state" /><em>*</em></label>
									<s:select id="register_state" name="paymentInfo.address.state">
										<s:option><fmt:message key="register.select.state" /> </s:option>
										<s:options-enumeration enum="org.yestech.lib.i18n.USStateEnum" />
									</s:select>
								</li>
								<li id="canada_provinces" style="display:none;">
									<label for="register_state"><fmt:message key="register.field.province" /><em>*</em></label>
									<s:select id="register_state" name="paymentInfo.address.state">
										<s:option><fmt:message key="register.select.state" /> </s:option>
										<s:options-enumeration enum="org.yestech.lib.i18n.CanadaProvinceEnum" />
									</s:select>
								</li>
								<li id="county" style="display:none;">
									<label for="register_county"><fmt:message key="register.field.county" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_county" name="paymentInfo.address.county" /></span>
								</li>
								<li>
									<label for="register_zipCode"><fmt:message key="register.field.zipCode" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_zipCode" name="paymentInfo.address.zipCode" /></span>
								</li>
								<li>
									<label for="register_creditCardNumber"><fmt:message key="register.field.creditCardNumber" /><em>*</em></label>
									<span class="textinput37"><s:text id="register_creditCardNumber" name="paymentInfo.creditCard.creditCardNumber" /></span>
								</li>
							</ul>
						</fieldset>
					</ui:roundedBox>
					<div class="button-panel">
						<button name="register" type="submit" class="bbform-button grey"><span>Save changes</span></button>
					</div>
				</s:form>
			</div>
			<div class="widget-bottom">
				<div class="widget-bottom-right"></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
