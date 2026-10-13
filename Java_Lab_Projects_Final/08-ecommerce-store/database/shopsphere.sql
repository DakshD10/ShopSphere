-- =====================================================================
-- ShopSphere E-Commerce Management System
-- RTU Advanced Java / Java Enterprise Capstone Project
-- Full 12-Table Relational Database Schema + Seed Data
-- =====================================================================

DROP DATABASE IF EXISTS shopsphere;
CREATE DATABASE shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

-- ---------------------------------------------------------------------
-- 1. USERS TABLE
-- Stores customers and administrator accounts with hashed passwords
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 2. ADDRESSES TABLE
-- Stores multiple shipping/billing addresses for customers
-- ---------------------------------------------------------------------
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    address_type ENUM('HOME', 'WORK', 'OTHER') NOT NULL DEFAULT 'HOME',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 3. CATEGORIES TABLE
-- Product classification categories
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 4. PRODUCTS TABLE
-- Master product catalog with stock, discount percentage, and images
-- ---------------------------------------------------------------------
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) 
        REFERENCES categories(category_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 5. CART TABLE
-- Persistent user cart backing the session cart
-- ---------------------------------------------------------------------
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) 
        REFERENCES products(product_id) ON DELETE CASCADE,
    CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 6. WISHLIST TABLE
-- Saved favorite products per customer
-- ---------------------------------------------------------------------
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) 
        REFERENCES products(product_id) ON DELETE CASCADE,
    CONSTRAINT uq_wishlist_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 7. ORDERS TABLE
-- Master order records with address snapshot and discount totals
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    payment_method ENUM('COD', 'CARD', 'UPI', 'NET_BANKING') NOT NULL DEFAULT 'COD',
    payment_status ENUM('PENDING', 'PAID', 'FAILED') NOT NULL DEFAULT 'PENDING',
    order_status ENUM('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) 
        REFERENCES addresses(address_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 8. ORDER_ITEMS TABLE
-- Line items for each order, capturing product snapshot and price
-- ---------------------------------------------------------------------
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) 
        REFERENCES products(product_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 9. PAYMENTS TABLE
-- Transaction references and mock payment logs
-- ---------------------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('SUCCESS', 'FAILED', 'PENDING') NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 10. REVIEWS TABLE
-- Product star ratings (1-5) and customer feedback
-- ---------------------------------------------------------------------
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) 
        REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 11. COUPONS TABLE
-- Promotional discount codes with validity and restrictions
-- ---------------------------------------------------------------------
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type ENUM('PERCENTAGE', 'FLAT') NOT NULL DEFAULT 'PERCENTAGE',
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    maximum_discount DECIMAL(10,2) NOT NULL DEFAULT 9999.00,
    expiry_date DATE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 12. COUPON_USAGE TABLE
-- Records coupon usage to prevent duplicate/fraudulent redemption
-- ---------------------------------------------------------------------
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon_usage_coupon FOREIGN KEY (coupon_id) 
        REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_usage_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_usage_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- =====================================================================
-- SEED DATA
-- Default passwords:
-- Admin:    admin@shopsphere.com    / admin123
-- Customer: student@shopsphere.com  / customer123
-- Salt:     shopsphere_salt_2026
-- =====================================================================

-- Users
INSERT INTO users (user_id, name, email, password, mobile, role, status) VALUES
(1, 'Administrator', 'admin@shopsphere.com', '8f44df33d42803b47af2902a9e734c16d340caa00666f1df9e09dcdf6bd01e77', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'Aryan Sharma', 'student@shopsphere.com', 'b8ffc81acaf143572ca88f34ec914abafd606b7078753af5e8aba2528e05ca8a', '9123456789', 'CUSTOMER', 'ACTIVE');

-- Addresses
INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type) VALUES
(1, 2, 'Hostel Room 402, Block C, Campus Grounds', 'Jaipur', 'Rajasthan', '302022', 'HOME'),
(2, 2, 'Tech Innovation Lab, Engineering Dept', 'Jaipur', 'Rajasthan', '302022', 'WORK');

-- Categories
INSERT INTO categories (category_id, name, description, status) VALUES
(1, 'Electronics', 'Laptops, wireless headphones, accessories and gadgets for students', TRUE),
(2, 'Footwear', 'Everyday sneakers, athletic running shoes and casual sandals', TRUE),
(3, 'Apparel', 'Comfortable hoodies, campus t-shirts and daily wear', TRUE),
(4, 'Accessories', 'Backpacks, water bottles, and desk accessories', TRUE),
(5, 'Books & Stationery', 'Engineering reference books, notebooks and organizers', TRUE);

-- Products
INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'SonicBoom ANC Wireless Headphones', 'AudioTech', 'Active noise cancellation with 40-hour battery life and Bluetooth 5.3', 2999.00, 10.00, 45, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', TRUE),
(2, 1, 'ProGrip Ergonomic Wireless Mouse', 'LogiCraft', 'High-precision 4000 DPI sensor with silent mechanical clicks and dual receiver', 799.00, 5.00, 60, 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600', TRUE),
(3, 1, 'UltraSlim 65W GaN Fast Charger', 'PowerPulse', 'Compact 3-port fast charger compatible with laptops, tablets and smartphones', 1299.00, 15.00, 30, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600', TRUE),
(4, 2, 'AeroStride Lightweight Running Shoes', 'StrideX', 'Breathable knit mesh with shock-absorbing cloud foam midsole', 1899.00, 20.00, 25, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600', TRUE),
(5, 3, 'Signature Campus Fleece Hoodie', 'UrbanWear', 'Heavyweight organic cotton hoodie with brushed interior fleece lining', 999.00, 0.00, 50, 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600', TRUE),
(6, 4, 'HydroShield Insulated Steel Bottle', 'KleenSip', 'Double-walled vacuum insulated 750ml flask, keeps cold for 24h', 499.00, 10.00, 75, 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600', TRUE),
(7, 4, 'Commuter Tech Backpack 28L', 'GearGuard', 'Water-resistant laptop compartment with dedicated cable organizer and anti-theft zipper', 1599.00, 15.00, 35, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600', TRUE),
(8, 5, 'Structured Hardcover Journal & Pen Set', 'ScriptMaster', 'Dotted 120 GSM bleed-resistant paper with smooth gel ink rollerball pen', 349.00, 0.00, 90, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600', TRUE);

-- Coupons
INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENTAGE', 10.00, 500.00, 300.00, '2027-12-31', TRUE),
(2, 'FLAT100', 'FLAT', 100.00, 800.00, 100.00, '2027-12-31', TRUE),
(3, 'FESTIVE20', 'PERCENTAGE', 20.00, 1500.00, 600.00, '2027-12-31', TRUE);

-- Reviews
INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 1, 5, 'Phenomenal audio clarity and the ANC is a lifesaver in the hostel library.'),
(2, 2, 2, 4, 'Very comfortable in hand and long battery life. Smooth glide on mousepad.');
