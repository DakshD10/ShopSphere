package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Section 25, 26, 38 (Practical Task 5) of Professor PDF:
 * LogoutServlet that destroys the active session using session.invalidate().
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        boolean hadSession = (session != null);

        if (hadSession) {
            // PDF requirement: invalidate the session
            session.invalidate();
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Logged Out</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        h1 { color: #dc2626; margin-top: 0; }");
        out.println("        .box { padding: 12px; border-radius: 6px; margin: 16px 0; background: #fef2f2; border-left: 4px solid #dc2626; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <h1>Logged Out Successfully</h1>");
        out.println("        <div class='box'>");
        if (hadSession) {
            out.println("            <p style='margin:0; color: #991b1b;'><code>session.invalidate()</code> was successfully executed.</p>");
            out.println("            <p style='margin:4px 0 0 0; color: #b91c1c; font-size: 0.85rem;'>The session has been bound out of service and its stored attributes destroyed.</p>");
        } else {
            out.println("            <p style='margin:0; color: #991b1b;'>No active session was found.</p>");
        }
        out.println("        </div>");
        out.println("        <p>If you now try to visit <a href='home'><code>/home</code></a>, the <code>AuthFilter</code> will block access and redirect you to the login page.</p>");
        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='login.html' class='btn'>Login Again</a>");
        out.println("            <a href='home' class='btn btn-outline'>Test Protected Home (/home)</a>");
        out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
