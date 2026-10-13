package com.servlet;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * Section 30 of Professor PDF:
 * HttpSessionListener implementation receiving notifications
 * when an HttpSession is created or destroyed.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Session Created");
        System.out.println("[SessionListener] ID: " + event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Session Destroyed");
        System.out.println("[SessionListener] ID: " + event.getSession().getId());
    }
}
