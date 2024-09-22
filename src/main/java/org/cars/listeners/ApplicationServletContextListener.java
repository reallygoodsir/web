package org.cars.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("ApplicationServletContextListener contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ApplicationServletContextListener contextDestroyed");
    }
}
