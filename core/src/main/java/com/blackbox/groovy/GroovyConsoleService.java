package com.blackbox.groovy;

import groovy.ui.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyConsoleService extends GroovyService {

    private static Logger logger = LoggerFactory.getLogger(GroovyConsoleService.class);

    public GroovyConsoleService() {
        super();
    }

    public void launch() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    new Console(createBinding()).run();
                }
                catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
    }
}