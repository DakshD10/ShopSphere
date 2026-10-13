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
 * Section 33.4 of Professor PDF:
 * HomeServlet - A protected servlet accessed via /home.
 * Retrieves username from HttpSession and renders welcome page.
 * Access is guarded by AuthFilter.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("username");
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Student Home</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        h2 { color: #15803d; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #dcfce7; color: #15803d; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-danger { background: #dc2626; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>Protected Resource: AuthFilter Passed</span>");
        out.println("        <h1>Student Home</h1>");
        out.println("        <h2>Welcome " + escapeHtml(username != null ? username : "Authenticated User") + "</h2>");
        out.println("        <p style='color: #475569;'>You have successfully accessed <code>/home</code> because <code>AuthFilter</code> verified that an active <code>HttpSession</code> with attribute <code>\"username\"</code> is present.</p>");
        out.println("        <div style='background: #f1f5f9; padding: 12px; border-radius: 6px; margin: 16px 0;'>");
        out.println("            <p style='margin:0; font-size: 0.85rem; color: #334155;'><strong>Session ID:</strong> <code>" + (session != null ? session.getId() : "N/A") + "</code></p>");
        out.println("            <p style='margin:4px 0 0 0; font-size: 0.85rem; color: #334155;'><strong>Session Creation Time:</strong> " + (session != null ? new java.util.Date(session.getCreationTime()) : "N/A") + "</p>");
        out.println("        </div>");
        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='logout' class='btn btn-danger'>Logout (Invalidate Session)</a>");
        out.println("            <a href='student-form.html' class='btn'>Student Form</a>");
        out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
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
