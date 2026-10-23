-- =============================================
-- shop_demo 整合版：业务表 + Spring Security 用户认证
-- 兼容 PostgreSQL / JdbcUserDetailsManager 默认查询
-- =============================================

-- 清理（仅练习用）
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

-- ---------------------------------------------
-- 1. 用户表（兼容 Spring Security）
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS users (
                                     id         BIGSERIAL    PRIMARY KEY,
                                     username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(500) NOT NULL,             -- 存 BCrypt 哈希，不是明文！
    email      VARCHAR(120) NOT NULL UNIQUE,
    full_name  VARCHAR(100),
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE, -- Security 默认字段
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
    );

COMMENT ON TABLE  users          IS '系统用户表（供 Spring Security + 业务共用）';
COMMENT ON COLUMN users.password IS 'BCrypt 编码后的密码';

-- ---------------------------------------------
-- 2. 权限表（Spring Security 默认表名/结构）
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS authorities (
                                           id        BIGSERIAL   PRIMARY KEY,
                                           username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    CONSTRAINT uk_authorities UNIQUE (username, authority)
    );

CREATE INDEX IF NOT EXISTS idx_authorities_username ON authorities(username);

COMMENT ON TABLE  authorities           IS '用户权限表（ROLE_USER / ROLE_ADMIN 等）';
COMMENT ON COLUMN authorities.authority IS '权限标识，通常以 ROLE_ 开头';

-- ---------------------------------------------
-- 3. 商品分类
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
                                          id         SERIAL      PRIMARY KEY,
                                          name       VARCHAR(50) NOT NULL UNIQUE,
    parent_id  INTEGER     REFERENCES categories(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- ---------------------------------------------
-- 4. 商品
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS products (
                                        id          BIGSERIAL     PRIMARY KEY,
                                        category_id INTEGER       REFERENCES categories(id) ON DELETE SET NULL,
    name        VARCHAR(150)  NOT NULL,
    sku         VARCHAR(50)   NOT NULL UNIQUE,
    price       NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    stock       INTEGER       NOT NULL DEFAULT 0 CHECK (stock >= 0),
    description TEXT,
    is_on_sale  BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
    );

CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_name     ON products(name);

-- ---------------------------------------------
-- 5. 订单
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
                                      id           BIGSERIAL     PRIMARY KEY,
                                      user_id      BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status       VARCHAR(20)   NOT NULL DEFAULT 'pending'
    CHECK (status IN ('pending','paid','shipped','completed','cancelled')),
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (total_amount >= 0),
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT now()
    );

CREATE INDEX IF NOT EXISTS idx_orders_user   ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);

-- ---------------------------------------------
-- 6. 订单明细
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
                                           id         BIGSERIAL     PRIMARY KEY,
                                           order_id   BIGINT        NOT NULL REFERENCES orders(id)   ON DELETE CASCADE,
    product_id BIGINT        NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
    quantity   INTEGER       NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10,2) NOT NULL CHECK (unit_price >= 0),
    UNIQUE (order_id, product_id)
    );

CREATE INDEX IF NOT EXISTS idx_order_items_order   ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON order_items(product_id);

-- ---------------------------------------------
-- 7. 自动维护 updated_at
-- ---------------------------------------------
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_users_updated_at ON users;
CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();