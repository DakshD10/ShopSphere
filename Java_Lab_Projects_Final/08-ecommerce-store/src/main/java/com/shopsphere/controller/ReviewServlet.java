package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller receiving customer product star ratings and text reviews.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/add-review"})
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO;

    @Override
    public void init() throws ServletException {
        this.reviewDAO = new ReviewDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String reviewText = req.getParameter("reviewText");

            if (rating < 1 || rating > 5) {
                rating = 5;
            }

            Review review = new Review(0, user.getUserId(), user.getName(), productId, rating, reviewText);
            reviewDAO.addReview(review);

            res.sendRedirect(req.getContextPath() + "/product-details?id=" + productId + "&review=success");

        } catch (NumberFormatException | SQLException e) {
            res.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
