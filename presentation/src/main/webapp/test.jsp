<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<html>
<head>

	<style type="text/css">

		/*

		.bb-custom-check is the class assigned to the link by default when calling to plugin.
		to user a different class, pass it in as an option when initiating the plugin

		$([selector]).customCheck({uiClass: 'classname'});

		*/

		.bb-custom-check {
			text-decoration:none;
		}

		.bb-custom-check span {
			display:inline-block;
			width:16px;
			line-height:16px;
			background:transparent url(${bb:libraryResource('/library/images/controls/checkbox.png')}) no-repeat top left;
		}
		
		.bb-custom-check.active span {
			background:transparent url(${bb:libraryResource('/library/images/controls/checkbox.png')}) no-repeat bottom left;
		}
	</style>
	<script src="${bb:libraryResource('/library/js/jquery-1.4.1.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery-ui-1.7.2.custom.min.js')}" type="text/javascript"></script>
	<%@ include file="/WEB-INF/jsp/include/blackbox_js.jspf" %>
	<script src="${bb:libraryResource('/library/js/core.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/bb.plugins.js')}" type="text/javascript"></script>

	<script type="text/javascript">

	$(function() {

		// create custom checkboxes for all things with the class of "poop"
		$(".poop").customCheck();

		// create an event handler for customClick event
		$("#frank").bind("customClick", function() {
			if($(this).is(":checked")) {
				$(".lala").customCheck('check');
			} else {
				$(".lala").customCheck('uncheck');
			}
		});
	});
	</script>

</head>
<body>

<pre>

<input id="frank" type="checkbox" class="poop"/>

<input id="cb1" type="checkbox" class="poop lala"/> <label for="cb1">Poop</label>
<input id="cb2" type="checkbox" class="poop lala"/> <label for="cb2">Poop</label>
<input id="cb3" type="checkbox" class="poop lala"/> <label for="cb3">Poop</label>



</pre>




</body>

</html>