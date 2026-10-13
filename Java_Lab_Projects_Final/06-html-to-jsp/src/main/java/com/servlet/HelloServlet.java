package com.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller Servlet for HTML to JSP Conversion Practical.
 * Mapped to /hello as specified in the professor's PDF.
 *
 * Request Flow:
 * student.jsp (View: Form)
 *   -> POST to /hello (Controller: HelloServlet)
 *   -> request.getParameter(...) reads form inputs
 *   -> request.setAttribute(...) stores data in request scope
 *   -> RequestDispatcher.forward(...) forwards request
 *   -> result.jsp (View: JSP Expression Language ${name}, etc.)
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If accessed directly via browser GET, redirect user to the student registration form
        response.sendRedirect("student.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ensure UTF-8 character encoding for multi-language input
        request.setCharacterEncoding("UTF-8");

        // Step 1: Read all 4 form parameters submitted from student.jsp
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        String age = request.getParameter("age");

        // Step 2: Store each parameter into request scope as an attribute
        request.setAttribute("name", name != null ? name.trim() : "");
        request.setAttribute("email", email != null ? email.trim() : "");
        request.setAttribute("course", course != null ? course.trim() : "");
        request.setAttribute("age", age != null ? age.trim() : "");

        // Step 3: Forward the request and response to result.jsp (JSP as View)
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
