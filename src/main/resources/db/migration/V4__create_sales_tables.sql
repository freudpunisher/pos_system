-- V4__create_sales_tables.sql
-- Create sales and sale_items tables

-- 1. Sales Table
CREATE TABLE IF NOT EXISTS sales (
                                     id BIGSERIAL PRIMARY KEY,
                                     sale_number VARCHAR(20) NOT NULL UNIQUE,
    cashier_id BIGINT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (subtotal >= 0),
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (tax >= 0),
    discount DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (discount >= 0),
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (total >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    completed_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sales_cashier
    FOREIGN KEY (cashier_id)
    REFERENCES users(id)
    ON DELETE RESTRICT
    );

CREATE INDEX idx_sales_sale_number ON sales(sale_number);
CREATE INDEX idx_sales_cashier_id ON sales(cashier_id);
CREATE INDEX idx_sales_status ON sales(status);
CREATE INDEX idx_sales_completed_at ON sales(completed_at);
CREATE INDEX idx_sales_created_at ON sales(created_at);

-- 2. Sale Items Table
CREATE TABLE IF NOT EXISTS sale_items (
                                          id BIGSERIAL PRIMARY KEY,
                                          sale_id BIGINT NOT NULL,
                                          product_id BIGINT NOT NULL,
                                          quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price > 0),
    total_price DECIMAL(10,2) NOT NULL CHECK (total_price > 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sale_items_sale
    FOREIGN KEY (sale_id)
    REFERENCES sales(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_sale_items_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE RESTRICT
    );

CREATE INDEX idx_sale_items_sale_id ON sale_items(sale_id);
CREATE INDEX idx_sale_items_product_id ON sale_items(product_id);

-- 3. Add triggers for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_categories_updated_at
    BEFORE UPDATE ON categories
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_sales_updated_at
    BEFORE UPDATE ON sales
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_sale_items_updated_at
    BEFORE UPDATE ON sale_items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sales IS 'Sales/transactions recorded in the POS system';
COMMENT ON TABLE sale_items IS 'Individual items within a sale';
COMMENT ON COLUMN sales.status IS 'PENDING, COMPLETED, VOIDED, REFUNDED';