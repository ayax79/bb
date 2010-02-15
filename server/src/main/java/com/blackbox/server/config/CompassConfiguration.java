package com.blackbox.server.config;

import org.compass.core.Compass;
import org.compass.core.CompassTemplate;
import org.compass.core.config.CompassEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author A.J. Wright
 */
@Configuration
public class CompassConfiguration {

    @Bean
    public Compass compass() {
        org.compass.core.config.CompassConfiguration conf = new org.compass.core.config.CompassConfiguration()
                .setSetting(CompassEnvironment.CONNECTION, "/var/cache/bbr/lucene")
                .addScan("com.blackbox");
        // defines an analyzer named whitespace for use
        conf.getSettings().setSetting("compass.engine.analyzer.whitespace.type", "Whitespace");

        return conf.buildCompass();
    }

    @Bean
    public CompassTemplate compassTemplate() {
        return new CompassTemplate(compass());
    }
    
}
