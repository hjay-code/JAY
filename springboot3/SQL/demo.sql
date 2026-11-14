CREATE TABLE  t_user
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    login_name VARCHAR(200) NULL DEFAULT NULL COMMENT '用户名',
    nick_name VARCHAR(200) NULL DEFAULT NULL COMMENT '昵称',
    passwd VARCHAR(200) NULL DEFAULT NULL COMMENT '密码',
    PRIMARY KEY (id)
);

INSERT into t_user(login_name, nick_name, passwd)
VALUES ("zhangsan", "张三", "123456");

SELECT *
FROM t_user tu;
LIMIT 100;