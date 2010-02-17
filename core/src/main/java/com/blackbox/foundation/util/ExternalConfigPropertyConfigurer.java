package com.blackbox.foundation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextPropertyPlaceholderConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author A.J. Wright
 */
public class ExternalConfigPropertyConfigurer extends ServletContextPropertyPlaceholderConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ExternalConfigPropertyConfigurer.class);

    private LinkedList<Resource> locations;
    private String external;
    private Properties properties;

    @Override
    public void setLocation(Resource location) {
        this.locations = new LinkedList<Resource>();
        this.locations.add(location);
        super.setLocation(location);
    }

    @Override
    public void setLocations(Resource[] locations) {
        this.locations = new LinkedList<Resource>(Arrays.asList(locations));
        super.setLocations(locations);
    }

    @Override
    protected Properties mergeProperties() throws IOException {


        if (this.external != null) {

            File externalFile = new File(external);
            if (externalFile.exists()) {
                if (this.locations == null) {
                    this.locations = new LinkedList<Resource>();
                }
                this.locations.add(new FileSystemResource(externalFile));
                setLocations(this.locations.toArray(new Resource[this.locations.size()]));
            }
        } else {
            logger.warn("No valid external file specified");
        }

        properties = super.mergeProperties();
        return properties;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getProperty(String key) {
        try {
            return properties.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }
}
