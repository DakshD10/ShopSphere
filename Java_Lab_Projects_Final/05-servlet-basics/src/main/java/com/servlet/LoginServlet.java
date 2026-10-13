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
 * Section 33.2 of Professor PDF:
 * LoginServlet validating username and password, creating HttpSession,
 * and storing "username" attribute upon successful validation.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to login.html form
        response.sendRedirect("login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Login Result</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        .success { color: #16a34a; }");
        out.println("        .error { color: #dc2626; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("        .box { padding: 12px; border-radius: 6px; margin: 16px 0; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");

        // PDF Section 33.2 credential check: "admin" and "1234"
        if ("admin".equals(username) && "1234".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            out.println("        <h1 class='success'>Login Successful</h1>");
            out.println("        <div class='box' style='background: #f0fdf4; border-left: 4px solid #16a34a;'>");
            out.println("            <p style='margin:0; color: #15803d;'><strong>Session Created:</strong> <code>session.setAttribute(\"username\", \"" + escapeHtml(username) + "\")</code></p>");
            out.println("            <p style='margin: 4px 0 0 0; color: #166534; font-size: 0.85rem;'>Session ID: <code>" + session.getId() + "</code></p>");
            out.println("        </div>");
            out.println("        <p>You can now access protected servlets guarded by <code>AuthFilter</code>.</p>");
            out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
            out.println("            <a href='home' class='btn'>Go to Home (Protected by AuthFilter)</a>");
            out.println("            <a href='logout' class='btn btn-outline'>Logout</a>");
            out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
            out.println("        </div>");
        } else {
            out.println("        <h1 class='error'>Invalid Login</h1>");
            out.println("        <div class='box' style='background: #fef2f2; border-left: 4px solid #dc2626;'>");
            out.println("            <p style='margin:0; color: #991b1b;'>Credentials did not match expected values.</p>");
            out.println("            <p style='margin: 4px 0 0 0; color: #b91c1c; font-size: 0.85rem;'>Hint: As specified in PDF Section 33.2, use <strong>admin</strong> / <strong>1234</strong>.</p>");
            out.println("        </div>");
            out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
            out.println("            <a href='login.html' class='btn'>Try Again</a>");
            out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
            out.println("        </div>");
        }

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
