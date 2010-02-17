package com.blackbox.groovy;

import groovy.lang.Binding;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Bruce Fancher
 */
public abstract class GroovyService implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected Logger logger = Logger.getLogger(getClass());

    private boolean launchAtStart;

    public GroovyService() {
        super();
    }

    public void launchInBackground() {
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                try {
                    launch();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        serverThread.setDaemon(true);
        serverThread.start();
    }

    public abstract void launch();

    protected Binding createBinding() {
        Binding binding = new Binding();
        binding.setVariable("context", applicationContext);

        return binding;
    }

    public void initialize() {
        if (Boolean.getBoolean("blackbox.console")) {
            launchInBackground();
        }
    }

    public void destroy() {
    }


    public boolean isLaunchAtStart() {
        return launchAtStart;
    }

    public void setLaunchAtStart(boolean launchAtStart) {
        this.launchAtStart = launchAtStart;
    }
}
