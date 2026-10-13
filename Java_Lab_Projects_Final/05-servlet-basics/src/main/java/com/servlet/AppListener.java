package com.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Section 29 & Practical Task 6 of Professor PDF:
 * ServletContextListener implementation receiving notifications
 * when application context is initialized (started) and destroyed (stopped).
 */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Application Started");
        // Also log to ServletContext log
        event.getServletContext().log("AppListener: Application Started successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Application Stopped");
        // Also log to ServletContext log
        event.getServletContext().log("AppListener: Application Stopped.");
    }
}
