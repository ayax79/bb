package com.blackbox.presentation.action.util;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpClient;

import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 *
 */
public class RecaptchaValidator {

    public static enum Response {
        SUCCESS(null),
        INVALID_PUBLIC_KEY("invalid-site-public-key"),
        INVALID_PRIVATE_KEY("invalid-site-private-key"),
        INVALID_REQUEST_COOKIE("invalid-request-cookie"),
        INCORRECT_CAPTCHA_SOLUTION("incorrect-captcha-sol"),
        VERIFY_PARAMETERS_INCORRECT("verify-params-incorrect"),
        INVALID_REFERRER("invalid-referrer"),
        CAPTCHA_NOT_REACHABLE("recaptcha-not-reachable"),
        UNKNOWN("unknown");

        private String code;

        Response(String code) {
            this.code = code;
        }

        public static Response valueOfCode(String code) {
            for (Response response : values()) {
                if (code.equals(response.code)) {
                    return response;
                }
            }
            throw new IllegalArgumentException("unknown code");
        }

        public String code() {
            return code;
        }
    }

    private String privateKey;


    public Response validate(String remoteip, String challenge, String response) throws IOException {
        assert remoteip != null;
        assert challenge != null;
        assert response != null;

        // fix for ip 6 on the mac during development
//        if ("0:0:0:0:0:0:0:1%0".equals(remoteip)) {
//            remoteip = "127.0.0.1";
//        }
//

        PostMethod m = new PostMethod("http://api-verify.recaptcha.net/verify");
        m.addParameter("privatekey", URLEncoder.encode(privateKey, "UTF-8"));
        m.addParameter("remoteip", URLEncoder.encode(remoteip, "UTF-8"));
        m.addParameter("challenge", URLEncoder.encode(challenge, "UTF-8"));
        m.addParameter("response", URLEncoder.encode(response, "UTF-8"));

        HttpClient client = new HttpClient();
        int i = client.executeMethod(m);
        if (i != 200) {
            return Response.CAPTCHA_NOT_REACHABLE;
        }

        String responseBody = m.getResponseBodyAsString();
        String[] items = responseBody.split("\n");

        if (items.length == 0) return Response.CAPTCHA_NOT_REACHABLE;

        if("true".equals(items[0])) return Response.SUCCESS;

        String errorCode = items[1];
        if (errorCode == null) return Response.CAPTCHA_NOT_REACHABLE;

        return Response.valueOfCode(errorCode);
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    
}
