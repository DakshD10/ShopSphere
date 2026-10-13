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
 * Section 38 Practical Tasks:
 * - Create a Student Form using POST.
 * - Display name, email and course using getParameter().
 * - Store username in HttpSession.
 */
@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect or forward to the student HTML form
        response.sendRedirect("student-form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Reading form parameters sent in HTTP POST body
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");

        // Storing username in HttpSession as requested in syllabus task
        if (name != null && !name.trim().isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("username", name.trim());
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Student Registration Result</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #e2e8f0; }");
        out.println("        th { background: #f1f5f9; color: #475569; font-weight: 600; width: 35%; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("        .session-info { background: #eff6ff; border-left: 4px solid #3b82f6; padding: 12px; margin-top: 16px; border-radius: 4px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <h1>Student Details Submitted (POST)</h1>");
        out.println("        <p>The following parameters were extracted using <code>request.getParameter(...)</code>:</p>");
        out.println("        <table>");
        out.println("            <tr><th>Field Name</th><th>Submitted Value</th><th>getParameter Call</th></tr>");
        out.println("            <tr><td>Student Name</td><td><strong>" + escapeHtml(name) + "</strong></td><td><code>request.getParameter(\"name\")</code></td></tr>");
        out.println("            <tr><td>Email Address</td><td><strong>" + escapeHtml(email) + "</strong></td><td><code>request.getParameter(\"email\")</code></td></tr>");
        out.println("            <tr><td>Enrolled Course</td><td><strong>" + escapeHtml(course) + "</strong></td><td><code>request.getParameter(\"course\")</code></td></tr>");
        out.println("        </table>");
        out.println("        <div class='session-info'>");
        out.println("            <strong>HttpSession Tracking:</strong><br>");
        out.println("            Student name <code>\"" + escapeHtml(name) + "\"</code> has been stored into <code>HttpSession</code> with attribute key <code>\"username\"</code>.");
        out.println("        </div>");
        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='student-form.html' class='btn'>&larr; Submit Another Student</a>");
        out.println("            <a href='home' class='btn'>Visit Protected Home (/home)</a>");
        out.println("            <a href='logout' class='btn btn-outline'>Logout</a>");
        out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    private String escapeHtml(String input) {
        if (input == null) return "<em>(null)</em>";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
