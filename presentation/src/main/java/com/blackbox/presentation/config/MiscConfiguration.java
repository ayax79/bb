package com.blackbox.presentation.config;

import com.blackbox.groovy.GroovyConsoleService;
import com.blackbox.presentation.action.util.DefaultEpxClient;
import com.blackbox.presentation.action.util.EpxClient;
import com.blackbox.foundation.util.GeoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.yestech.episodic.DefaultEpisodicService;
import org.yestech.episodic.EpisodicService;
import org.yestech.rpx.DefaultRPXClient;
import org.yestech.rpx.RPXClient;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Lazy
@Configuration
public class MiscConfiguration {

    @Value("${rpx.realm}")
    String rpxRealm;

    @Value("${rpx.api.key}")
    String rpxApiKey;

    @Value("${google.maps.apiKey")
    String googleMapKey;

    @Value("${upload.tmp.directory}")
    String tmpDirectoryPath;

    @Value("${episodic.secret}")
    String episodicSecret;

    @Value("${episodic.api.key}")
    String episodicApiKey;

    @Bean
    public RPXClient rpxClient() {
        return new DefaultRPXClient(rpxApiKey, rpxRealm);
    }

    @Bean
    public GeoUtil geoUtil() {
        GeoUtil geoUtil = new GeoUtil();
        geoUtil.setGoogleKey(googleMapKey);
        return geoUtil;
    }

    @Bean
    public EpxClient epxClient() {
        return new DefaultEpxClient();
    }

    @Bean
    public File uploadTempDir() {
        return new File(tmpDirectoryPath);
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService threadPool() {
        return new ThreadPoolExecutor(0, 1000, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    @Bean(destroyMethod = "destroy")
    public EpisodicService episodicService() {
        return new DefaultEpisodicService(episodicSecret, episodicApiKey);
    }

    @Bean(initMethod = "initialize")
    public GroovyConsoleService groovyConsole() {
        return new GroovyConsoleService();
    }

}
