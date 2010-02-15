package com.blackbox.server.util;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.yestech.notify.util.Sl4jLogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.io.StringWriter;


public final class VelocityUtil {

    private static final Logger logger = LoggerFactory.getLogger(VelocityUtil.class);

    private VelocityUtil() {
    }
    
    public static String transform(String path, Map<String, ?> map) {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new Sl4jLogChute());
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();
            VelocityContext context = new VelocityContext(map);
            Template template = ve.getTemplate(path);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
