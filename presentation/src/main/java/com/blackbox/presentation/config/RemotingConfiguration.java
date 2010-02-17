package com.blackbox.presentation.config;

import com.blackbox.foundation.IEntityManager;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.billing.IBillingManager;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.gifting.IGiftingManager;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.security.IAuthenticationManager;
import com.blackbox.foundation.security.IAuthorizationManager;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.user.IUserManager;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.SerializerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.yestech.lib.hessian.joda.JodaSerializerFactory;

import java.net.MalformedURLException;

@Lazy
@Configuration
public class RemotingConfiguration {

    @Value("${server.host}")
    String serverHost;

    @Bean
    public SerializerFactory hessianSerializerFactory() {
        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(new JodaSerializerFactory());
        return serializerFactory;
    }

    @Bean
    public HessianProxyFactory hessianProxyFactory() {
        HessianProxyFactory proxyFactory = new HessianProxyFactory();
        proxyFactory.setSerializerFactory(hessianSerializerFactory());
        return proxyFactory;
    }

    @Bean
    public IAuthenticationManager authenticationManager() {
        return proxy(IAuthenticationManager.class, "AuthenticationManager");
    }

    @Bean
    public IAuthorizationManager authorizationManager() {
        return proxy(IAuthorizationManager.class, "AuthorizationManager");
    }

    @Bean
    public IUserManager userManager() {
        return proxy(IUserManager.class, "UserManager");
    }

    @Bean
    public IMediaManager mediaManager() {
        return proxy(IMediaManager.class, "MediaManager");
    }

    @Bean
    public ISocialManager socialManager() {
        return proxy(ISocialManager.class, "SocialManager");
    }

    @Bean
    public IEntityManager entityManager() {
        return proxy(IEntityManager.class, "EntityManager");
    }

    @Bean
    public IOccasionManager occasionManager() {
        return proxy(IOccasionManager.class, "OccasionManager");
    }

    @Bean
    public IBookmarkManager bookmarkManager() {
        return proxy(IBookmarkManager.class, "BookmarkManager");
    }

    @Bean
    public IMessageManager messageManager() {
        return proxy(IMessageManager.class, "MessageManager");
    }

    @Bean
    public IActivityManager activityManager() {
        return proxy(IActivityManager.class, "ActivityManager");
    }

    @Bean
    public IBillingManager billingManager() {
        return proxy(IBillingManager.class, "BillingManager");
    }

    @Bean
    public IGiftingManager giftingManager() {
        return proxy(IGiftingManager.class, "GiftingManager");
    }

    @SuppressWarnings({"unchecked"})
    private <T> T proxy(Class<T> clazz, String name) {
        try {
            return (T) hessianProxyFactory().create(clazz, serverUrl(name));
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }


    private String serverUrl(String name) {
        return serverHost + "/remoting/" + name;
    }

}
