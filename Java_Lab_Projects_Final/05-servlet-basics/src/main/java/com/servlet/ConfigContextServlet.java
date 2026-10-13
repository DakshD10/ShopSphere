package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Section 20, 21, 22 of Professor PDF:
 * Demonstrating ServletConfig vs ServletContext:
 * - ServletConfig: Configuration information specific to one Servlet
 * - ServletContext: Context representing the entire web application
 */
@WebServlet(
    urlPatterns = "/config-context",
    initParams = {
        @WebInitParam(name = "servletSpecificEmail", value = "support@servletlab.edu"),
        @WebInitParam(name = "logLevel", value = "DEBUG")
    }
)
public class ConfigContextServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. ServletConfig (Servlet-specific configuration)
        ServletConfig config = getServletConfig();
        String servletEmail = config.getInitParameter("servletSpecificEmail");
        String logLevel = config.getInitParameter("logLevel");
        String servletName = config.getServletName();

        // 2. ServletContext (Application-wide configuration & environment)
        ServletContext context = getServletContext();
        String appTitle = context.getInitParameter("appTitle");
        if (appTitle == null) appTitle = "Java Servlet Lab Application";
        String serverInfo = context.getServerInfo();
        String contextPath = context.getContextPath();
        int majorVersion = context.getEffectiveMajorVersion();
        int minorVersion = context.getEffectiveMinorVersion();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>ServletConfig vs ServletContext</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 750px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("        th, td { padding: 10px 14px; text-align: left; border-bottom: 1px solid #e2e8f0; }");
        out.println("        th { background: #f1f5f9; color: #334155; font-weight: 600; }");
        out.println("        .code { font-family: monospace; color: #0369a1; background: #f0f9ff; padding: 2px 6px; border-radius: 4px; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #e0f2fe; color: #0369a1; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>Syllabus Section 20, 21, 22</span>");
        out.println("        <h1>ServletConfig vs ServletContext</h1>");
        out.println("        <p>Comparison between Servlet-specific configuration and application-wide context:</p>");

        out.println("        <table>");
        out.println("            <tr><th>Feature (PDF Sec 22)</th><th>ServletConfig</th><th>ServletContext</th></tr>");
        out.println("            <tr><td><strong>Scope</strong></td><td>One Servlet only</td><td>Entire web application</td></tr>");
        out.println("            <tr><td><strong>Purpose</strong></td><td>Servlet-specific configuration</td><td>Application-wide information & resources</td></tr>");
        out.println("            <tr><td><strong>Access Method</strong></td><td><code>getServletConfig()</code></td><td><code>getServletContext()</code></td></tr>");
        out.println("            <tr><td><strong>Sample Value</strong></td><td><span class='code'>servletSpecificEmail: " + servletEmail + "</span><br><span class='code'>logLevel: " + logLevel + "</span></td><td><span class='code'>appTitle: " + appTitle + "</span><br><span class='code'>Server: " + serverInfo + "</span></td></tr>");
        out.println("        </table>");

        out.println("        <h3>Live Inspected Values:</h3>");
        out.println("        <ul>");
        out.println("            <li><strong>Servlet Name:</strong> " + servletName + "</li>");
        out.println("            <li><strong>Context Path:</strong> " + contextPath + "</li>");
        out.println("            <li><strong>Container Server Info:</strong> " + serverInfo + "</li>");
        out.println("            <li><strong>Effective Servlet Spec:</strong> Jakarta Servlet " + majorVersion + "." + minorVersion + "</li>");
        out.println("        </ul>");

        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='index.html' class='btn btn-outline'>&larr; Back to Portal</a>");
        out.println("            <a href='cookies' class='btn'>Cookies vs Session Demo &rarr;</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
