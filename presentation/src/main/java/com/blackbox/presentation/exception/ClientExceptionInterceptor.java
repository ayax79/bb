package com.blackbox.presentation.exception;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ClassUtils;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * If an exception has the header error-class it will be translated to the correct blackbox exception.
 *
 * @author A.J. Wright
 */
@Provider
@ClientInterceptor
@Component("client-exception-interceptor")
public class ClientExceptionInterceptor implements ClientExecutionInterceptor {
    @SuppressWarnings({"unchecked"})
    @Override
    public ClientResponse execute(ClientExecutionContext ctx) throws Exception {
        ClientResponse clientResponse = ctx.proceed();
        if (clientResponse.getResponseStatus().getFamily() != Response.Status.Family.SUCCESSFUL) {
            MultivaluedMap map = clientResponse.getHeaders();
            if (map != null) {

                List<String> classes= (List<String>) map.get("error-class");
                if (classes != null && !classes.isEmpty()) {
                    List<String> messages = (List<String>) map.get("error-message");
                    Class clazz = ClassUtils.getClass(classes.get(0));
                    if (messages != null && !messages.isEmpty()) {
                        Constructor c = ConstructorUtils.getAccessibleConstructor(clazz, String.class);
                        throw (Exception) c.newInstance(messages.get(0));
                    } else {
                        throw (Exception) clazz.newInstance();
                    }
                }
            }
        }
        return clientResponse;
    }
}
