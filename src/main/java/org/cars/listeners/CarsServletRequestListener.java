package org.cars.listeners;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class CarsServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("CarsServletRequestListener requestDestroyed");
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("CarsServletRequestListener requestInitialized");
    }
}
