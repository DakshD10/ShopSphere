package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Section 24 & 27 of Professor PDF:
 * Demonstrating Cookies and Cookie vs HttpSession:
 * - Adding a Cookie using response.addCookie()
 * - Reading Cookies using request.getCookies()
 * - Contrasting client-side Cookies with server-side HttpSession
 */
@WebServlet("/cookies")
public class CookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String cookieName = request.getParameter("name");
        String cookieVal = request.getParameter("val");

        String feedback = null;

        if ("set".equalsIgnoreCase(action)) {
            String val = (cookieVal != null && !cookieVal.trim().isEmpty()) ? cookieVal.trim() : "Jahanvi";
            Cookie cookie = new Cookie("username", val);
            cookie.setMaxAge(3600); // 1 hour
            cookie.setPath(request.getContextPath() + "/");
            response.addCookie(cookie);
            feedback = "Cookie created and added to response: <code>username = " + escapeHtml(val) + "</code>";
        } else if ("clear".equalsIgnoreCase(action)) {
            Cookie cookie = new Cookie("username", "");
            cookie.setMaxAge(0); // Delete cookie
            cookie.setPath(request.getContextPath() + "/");
            response.addCookie(cookie);
            feedback = "Cookie <code>username</code> marked for immediate deletion (maxAge = 0).";
        }

        // Reading cookies from request as shown in PDF Section 24
        Cookie[] cookies = request.getCookies();
        String foundUsername = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    foundUsername = c.getValue();
                    break;
                }
            }
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Cookie Demonstration</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 750px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        table { width: 100%; border-collapse: collapse; margin: 18px 0; }");
        out.println("        th, td { padding: 10px 14px; text-align: left; border-bottom: 1px solid #e2e8f0; }");
        out.println("        th { background: #f1f5f9; color: #334155; font-weight: 600; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #e0f2fe; color: #0369a1; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-danger { background: #dc2626; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("        .alert { padding: 12px; border-radius: 6px; margin: 12px 0; background: #f0fdf4; border-left: 4px solid #16a34a; color: #15803d; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>Syllabus Section 24 & 27</span>");
        out.println("        <h1>Cookies & Session Comparison</h1>");

        if (feedback != null) {
            out.println("        <div class='alert'>" + feedback + "</div>");
        }

        out.println("        <div style='background: #f8fafc; border: 1px solid #e2e8f0; padding: 16px; border-radius: 8px; margin: 16px 0;'>");
        out.println("            <h3 style='margin-top:0;'>Current Cookies in Request:</h3>");
        if (cookies != null && cookies.length > 0) {
            out.println("            <table>");
            out.println("                <tr><th>Cookie Name</th><th>Cookie Value</th></tr>");
            for (Cookie c : cookies) {
                out.println("                <tr><td><code>" + escapeHtml(c.getName()) + "</code></td><td>" + escapeHtml(c.getValue()) + "</td></tr>");
            }
            out.println("            </table>");
        } else {
            out.println("            <p style='color: #64748b;'>No cookies found in incoming request headers.</p>");
        }

        if (foundUsername != null) {
            out.println("            <p style='color: #15803d;'><strong>Found 'username' cookie value:</strong> " + escapeHtml(foundUsername) + "</p>");
        }
        out.println("        </div>");

        out.println("        <div>");
        out.println("            <a href='cookies?action=set&val=Jahanvi' class='btn'>Set Cookie (name=\"username\", val=\"Jahanvi\")</a>");
        out.println("            <a href='cookies?action=clear' class='btn btn-danger'>Clear Cookie</a>");
        out.println("            <a href='cookies' class='btn btn-outline'>Re-read Cookies</a>");
        out.println("        </div>");

        out.println("        <h3 style='margin-top: 28px;'>Cookie vs HttpSession (PDF Section 27):</h3>");
        out.println("        <table>");
        out.println("            <tr><th>Feature</th><th>Cookie</th><th>HttpSession</th></tr>");
        out.println("            <tr><td><strong>Typical storage</strong></td><td>Client-associated (browser)</td><td>Server-side session data</td></tr>");
        out.println("            <tr><td><strong>Servlet API</strong></td><td><code>jakarta.servlet.http.Cookie</code></td><td><code>jakarta.servlet.http.HttpSession</code></td></tr>");
        out.println("            <tr><td><strong>Example</strong></td><td>username cookie</td><td>logged-in user information</td></tr>");
        out.println("        </table>");

        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='index.html' class='btn btn-outline'>&larr; Back to Portal</a>");
        out.println("            <a href='login.html' class='btn'>HttpSession Login Flow &rarr;</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
