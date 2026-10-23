-- 用户（密码统一为 "password" 的 BCrypt 哈希，仅供测试！）
-- 生产环境务必用 BCryptPasswordEncoder 生成自己的哈希
INSERT INTO users (username, password, email, full_name) VALUES
                                                             ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
                                                              'admin@example.com', '管理员'),
                                                             ('alice', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
                                                              'alice@example.com', 'Alice'),
                                                             ('bob',   '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
                                                              'bob@example.com',   'Bob');

-- 权限
INSERT INTO authorities (username, authority) VALUES
                                                  ('admin', 'ROLE_USER'),
                                                  ('admin', 'ROLE_ADMIN'),
                                                  ('alice', 'ROLE_USER'),
                                                  ('bob',   'ROLE_USER');

-- 业务数据
INSERT INTO categories (name) VALUES ('电子产品'), ('图书'), ('服装');

INSERT INTO products (category_id, name, sku, price, stock, description) VALUES
                                                                             (1, '无线鼠标',        'SKU-MOUSE-001',  89.90, 100, '2.4G 无线鼠标'),
                                                                             (1, '机械键盘',        'SKU-KEY-001',   299.00,  50, '青轴 87 键'),
                                                                             (2, 'PostgreSQL 入门', 'SKU-BOOK-001',   59.00,  30, '数据库入门好书'),
                                                                             (3, '纯棉T恤',         'SKU-TSHIRT-001', 39.00, 200, '夏季基础款');

INSERT INTO orders (user_id, status, total_amount) VALUES
                                                       (2, 'paid',    388.90),
                                                       (3, 'pending',  59.00);

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
                                                                         (1, 1, 1,  89.90),
                                                                         (1, 2, 1, 299.00),
                                                                         (2, 3, 1,  59.00);