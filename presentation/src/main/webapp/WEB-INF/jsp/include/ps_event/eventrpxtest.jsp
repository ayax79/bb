<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<head>
	<script>
	 $(document).ready(function(){
         $("#google_but").click(function() {
             $("#google_form").submit();
         })
         $("#mslive_but").click(function() {
             $("#mslive_form").submit();
         })
     })

	</script>
</head>
<body>

	<button id="google_but">google</button> <br/>
	<button id="mslive_but">mslive</button><br/>
	
	
	<form id="google_form" method="POST" action='https://blackbox-republic.rpxnow.com/openid/start?token_url=${actionBean.encodedUrl}'>
        <input type="hidden" name="openid_identifier" value="https://www.google.com/accounts/o8/id" />
    </form>
    
    <form id="mslive_form" method="POST" action='https://blackbox-republic.rpxnow.com/liveid/start?token_url=${actionBean.encodedUrl}'>
    </form>
    
    
</body>

