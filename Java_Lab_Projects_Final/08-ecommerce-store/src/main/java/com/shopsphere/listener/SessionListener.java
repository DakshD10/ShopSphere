package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Session lifecycle listener matching Section 15 of the specification.
 * Tracks concurrent active sessions for administrative diagnostics.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        ServletContext ctx = se.getSession().getServletContext();
        ctx.setAttribute("activeSessionsCount", count);
        System.out.println(" [ShopSphere] Session created (ID: " + se.getSession().getId() + "). Active sessions: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        if (count < 0) {
            count = 0;
            ACTIVE_SESSIONS.set(0);
        }
        ServletContext ctx = se.getSession().getServletContext();
        ctx.setAttribute("activeSessionsCount", count);
        System.out.println(" [ShopSphere] Session destroyed (ID: " + se.getSession().getId() + "). Active sessions: " + count);
    }

    public static int getActiveSessions() {
        return ACTIVE_SESSIONS.get();
    }
}
