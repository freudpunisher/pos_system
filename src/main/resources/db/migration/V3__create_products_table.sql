-- V3__create_products_table.sql
-- Create the products table

CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        sku VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    quantity_in_stock INTEGER NOT NULL DEFAULT 0 CHECK (quantity_in_stock >= 0),
    reorder_level INTEGER NOT NULL DEFAULT 10 CHECK (reorder_level >= 0),
    category_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL
    );

CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_name ON products(name);

-- Partial index for low stock products
CREATE INDEX idx_products_low_stock ON products(quantity_in_stock)
    WHERE quantity_in_stock < reorder_level AND active = true;

COMMENT ON TABLE products IS 'Inventory items available for sale';
COMMENT ON COLUMN products.sku IS 'Stock Keeping Unit - unique identifier';
COMMENT ON COLUMN products.quantity_in_stock IS 'Current inventory quantity';
COMMENT ON COLUMN products.reorder_level IS 'Minimum quantity before reorder alert';