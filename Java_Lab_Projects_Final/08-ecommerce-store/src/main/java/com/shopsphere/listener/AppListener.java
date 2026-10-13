package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Date;

/**
 * Application lifecycle listener matching Section 15 of the specification.
 * Initializes application-wide context parameters and logs container lifecycle events.
 */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute("appName", "ShopSphere");
        ctx.setAttribute("appVersion", "1.0.0");
        ctx.setAttribute("appStartedAt", new Date());
        ctx.setAttribute("currencySymbol", "₹");

        System.out.println("==========================================================");
        System.out.println(" [ShopSphere] ServletContext initialized successfully!");
        System.out.println(" [ShopSphere] Context Path: " + ctx.getContextPath());
        System.out.println(" [ShopSphere] Server Info : " + ctx.getServerInfo());
        System.out.println("==========================================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(" [ShopSphere] ServletContext shutting down cleanly.");
    }
}
