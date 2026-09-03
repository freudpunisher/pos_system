-- V5__insert_default_data.sql
-- Insert default/seed data for the system

-- 1. Create default admin user
INSERT INTO users (username, email, password, full_name, role)
VALUES
    ('admin', 'admin@pos.com', 'Admin@123', 'System Administrator', 'ADMIN'),
    ('manager', 'manager@pos.com', 'Manager@123', 'Store Manager', 'MANAGER'),
    ('cashier1', 'cashier1@pos.com', 'Cashier@123', 'John Cashier', 'CASHIER');

-- 2. Create default categories
INSERT INTO categories (name, description)
VALUES
    ('Electronics', 'Electronic devices and accessories'),
    ('Clothing', 'Apparel and fashion items'),
    ('Food & Beverages', 'Groceries and drinks'),
    ('Books', 'Printed and digital books'),
    ('Home & Garden', 'Home improvement and garden supplies');

-- 3. Create sample products
-- Electronics
INSERT INTO products (sku, name, description, price, quantity_in_stock, reorder_level, category_id)
VALUES
    ('ELEC-001', 'Smartphone X', 'Latest smartphone with 5G', 699.99, 50, 10,
     (SELECT id FROM categories WHERE name = 'Electronics')),
    ('ELEC-002', 'Laptop Pro', 'High performance laptop', 1299.99, 30, 5,
     (SELECT id FROM categories WHERE name = 'Electronics')),
    ('ELEC-003', 'Wireless Headphones', 'Noise cancelling headphones', 199.99, 100, 20,
     (SELECT id FROM categories WHERE name = 'Electronics'));

-- Clothing
INSERT INTO products (sku, name, description, price, quantity_in_stock, reorder_level, category_id)
VALUES
    ('CLOTH-001', 'T-Shirt Classic', '100% cotton t-shirt', 19.99, 200, 50,
     (SELECT id FROM categories WHERE name = 'Clothing')),
    ('CLOTH-002', 'Jeans Slim Fit', 'Blue denim jeans', 49.99, 150, 30,
     (SELECT id FROM categories WHERE name = 'Clothing')),
    ('CLOTH-003', 'Winter Jacket', 'Waterproof winter jacket', 89.99, 75, 15,
     (SELECT id FROM categories WHERE name = 'Clothing'));

-- Food & Beverages
INSERT INTO products (sku, name, description, price, quantity_in_stock, reorder_level, category_id)
VALUES
    ('FOOD-001', 'Organic Coffee Beans', 'Fresh roasted coffee', 14.99, 500, 100,
     (SELECT id FROM categories WHERE name = 'Food & Beverages')),
    ('FOOD-002', 'Green Tea', 'Premium green tea leaves', 9.99, 300, 50,
     (SELECT id FROM categories WHERE name = 'Food & Beverages'));

-- Books
INSERT INTO products (sku, name, description, price, quantity_in_stock, reorder_level, category_id)
VALUES
    ('BOOK-001', 'Java Programming Guide', 'Complete Java reference', 39.99, 45, 10,
     (SELECT id FROM categories WHERE name = 'Books')),
    ('BOOK-002', 'The Art of War', 'Classic strategy book', 24.99, 30, 5,
     (SELECT id FROM categories WHERE name = 'Books'));

-- 4. Create a sample sale
INSERT INTO sales (sale_number, cashier_id, status, completed_at)
VALUES
    ('SALE-20240115-0001', (SELECT id FROM users WHERE username = 'cashier1'), 'COMPLETED',
     CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- 5. Add sample sale items
INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, total_price)
VALUES
    ((SELECT id FROM sales WHERE sale_number = 'SALE-20240115-0001'),
     (SELECT id FROM products WHERE sku = 'ELEC-001'), 2, 699.99, 1399.98),
    ((SELECT id FROM sales WHERE sale_number = 'SALE-20240115-0001'),
     (SELECT id FROM products WHERE sku = 'CLOTH-001'), 3, 19.99, 59.97);

-- 6. Update sale totals
UPDATE sales
SET subtotal = (SELECT SUM(total_price) FROM sale_items WHERE sale_id = sales.id),
    tax = subtotal * 0.10,
    total = subtotal + tax
WHERE sale_number = 'SALE-20240115-0001';