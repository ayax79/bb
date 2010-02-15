<%@ tag language="java" %>
<%@ attribute name="publicKey" required="true" %>
<%@ attribute name="jsVar" required="true" %>
<%@ attribute name="error" required="false" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<script type="text/javascript"
        src="http://api.recaptcha.net/challenge?k=${publicKey}&error=${error}">
</script>

<noscript>

    <c:choose>
        <c:when test="${error != null}" >
            <iframe src="http://api.recaptcha.net/noscript?k=${publicKey}&error=${error}"
                    height="300" width="500" frameborder="0"></iframe>
        </c:when>
        <c:otherwise>
            <iframe src="http://api.recaptcha.net/noscript?k=${publicKey}"
                    height="300" width="500" frameborder="0"></iframe>
        </c:otherwise>
    </c:choose>
    <br>

    <textarea name="recaptcha_challenge_field" rows="3" cols="40">
    </textarea>
    <input type="hidden" name="recaptcha_response_field"
           value="manual_challenge">
</noscript>


