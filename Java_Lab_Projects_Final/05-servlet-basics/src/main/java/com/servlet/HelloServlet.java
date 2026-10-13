package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Section 12, 15, 16 of Professor PDF:
 * First Servlet demonstrating:
 * - Extending HttpServlet
 * - @WebServlet("/hello") mapping
 * - Handling GET via doGet()
 * - Handling POST via doPost()
 * - Reading query parameters / form parameters via request.getParameter()
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>HelloServlet - GET</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        h2 { color: #334155; font-weight: 500; font-size: 1.25rem; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #e0f2fe; color: #0369a1; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>HTTP Method: GET</span>");
        out.println("        <h1>Hello Students!</h1>");
        out.println("        <h2>Welcome to Servlet Programming</h2>");

        if (name != null && !name.trim().isEmpty()) {
            out.println("        <div style='background: #f0fdf4; border-left: 4px solid #16a34a; padding: 12px; margin: 16px 0; border-radius: 4px;'>");
            out.println("            <h3 style='margin:0; color: #15803d;'>Welcome " + escapeHtml(name) + "!</h3>");
            out.println("            <p style='margin: 4px 0 0 0; color: #166534; font-size: 0.9rem;'>Received name parameter via GET query string (request.getParameter(\"name\")).</p>");
            out.println("        </div>");
        } else {
            out.println("        <p style='color: #64748b;'>No query parameter supplied. Try passing <code>?name=YourName</code> or use the forms on the home page.</p>");
        }

        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='index.html' class='btn btn-outline'>&larr; Back to Portal</a>");
        out.println("            <a href='student-form.html' class='btn'>Go to Student Form (POST)</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>HelloServlet - POST</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 650px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #fef3c7; color: #b45309; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>HTTP Method: POST</span>");
        out.println("        <h1>Welcome " + escapeHtml(name != null ? name : "Student") + "</h1>");
        out.println("        <p style='color: #475569;'>Processed through <code>doPost()</code> using <code>request.getParameter(\"name\")</code>.</p>");
        out.println("        <p style='color: #64748b; font-size: 0.9rem;'>Notice: In POST requests, parameters are sent in the HTTP request body instead of the URL.</p>");
        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='index.html' class='btn btn-outline'>&larr; Back to Portal</a>");
        out.println("            <a href='student-form.html' class='btn'>Student Form (3 fields)</a>");
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
