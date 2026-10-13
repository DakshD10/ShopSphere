package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Section 18 & 19 of Professor PDF:
 * Demonstrating the Servlet Life Cycle:
 * 1. Loading & Instantiation
 * 2. init() - initialization (called once)
 * 3. service() - request dispatch to doGet/doPost
 * 4. destroy() - cleanup upon undeploy / container shutdown
 */
@WebServlet(urlPatterns = "/lifecycle", loadOnStartup = 1)
public class LifeCycleServlet extends HttpServlet {

    private String initTime;
    private final AtomicInteger serviceCount = new AtomicInteger(0);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        System.out.println("Servlet Initialized (init() called at " + initTime + ")");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int currentCount = serviceCount.incrementAndGet();
        System.out.println("service() called -> Request #" + currentCount);
        // Delegate to standard HttpServlet.service dispatch (which invokes doGet/doPost)
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Servlet Lifecycle Demonstration</title>");
        out.println("    <style>");
        out.println("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 40px; background: #f8fafc; color: #1e293b; }");
        out.println("        .card { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); max-width: 700px; }");
        out.println("        h1 { color: #0284c7; margin-top: 0; }");
        out.println("        .flow-step { background: #f1f5f9; border-left: 4px solid #0284c7; padding: 10px 14px; margin: 8px 0; border-radius: 4px; }");
        out.println("        .flow-step strong { color: #0f172a; }");
        out.println("        .badge { display: inline-block; padding: 4px 10px; background: #e0f2fe; color: #0369a1; border-radius: 6px; font-weight: 600; font-size: 0.85rem; }");
        out.println("        .btn { display: inline-block; padding: 8px 16px; margin: 6px 4px; background: #0284c7; color: white; text-decoration: none; border-radius: 6px; font-size: 0.9rem; }");
        out.println("        .btn-outline { background: transparent; color: #0284c7; border: 1px solid #0284c7; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='card'>");
        out.println("        <span class='badge'>Syllabus Section 18 & 19</span>");
        out.println("        <h1>Servlet Life Cycle</h1>");
        out.println("        <p>The container controls the lifecycle; application code does not normally call these methods directly.</p>");

        out.println("        <div style='background: #eff6ff; padding: 14px; border-radius: 8px; margin: 16px 0;'>");
        out.println("            <p style='margin:0; font-weight:600; color: #1e40af;'>Live Container Metrics:</p>");
        out.println("            <p style='margin:4px 0 0 0;'>• <strong>init() invocation timestamp:</strong> " + initTime + "</p>");
        out.println("            <p style='margin:4px 0 0 0;'>• <strong>service() execution count:</strong> " + serviceCount.get() + " request(s)</p>");
        out.println("        </div>");

        out.println("        <h3>Lifecycle Phases (PDF Section 18 & 19):</h3>");
        out.println("        <div class='flow-step'>");
        out.println("            <strong>1. Loading & Instantiation:</strong> Servlet container loads class and creates an instance.");
        out.println("        </div>");
        out.println("        <div class='flow-step'>");
        out.println("            <strong>2. init():</strong> Called once when initialized by container. Allocates resources.");
        out.println("        </div>");
        out.println("        <div class='flow-step'>");
        out.println("            <strong>3. service():</strong> Receives each client request and dispatches to doGet() or doPost().");
        out.println("        </div>");
        out.println("        <div class='flow-step'>");
        out.println("            <strong>4. doGet() / doPost():</strong> Contains request handling and response generation logic.");
        out.println("        </div>");
        out.println("        <div class='flow-step'>");
        out.println("            <strong>5. destroy():</strong> Called when servlet is taken out of service to release resources.");
        out.println("        </div>");

        out.println("        <div style='margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;'>");
        out.println("            <a href='lifecycle' class='btn'>Refresh Page (Trigger service())</a>");
        out.println("            <a href='index.html' class='btn btn-outline'>Portal Home</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet Destroyed (destroy() called)");
    }
}
