package com.hjay.sdp.principles.lsp.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试类
 *  里氏替换原则
 *  修改案例
 *      Square 不再 继承 Rectangle ，而是两个类都实现一个 四边形 Quadrilateral 接口
 *      扩展方法就不能传入 Square 来保证程序的正常执行
 */
@Slf4j
public class RectangleDemo {

    public static void main(String[] args) {
        RectangleDemo rectangleDemo = new RectangleDemo();

        // 创建长方形
        Rectangle rectangle = new Rectangle();
        rectangle.setLength(20);
        rectangle.setWidth(10);

        // 调用扩宽方法
        rectangleDemo.resize(rectangle);

        // 打印
        rectangleDemo.print(rectangle);
    }

    /**
     * 扩宽方法
     * @param rectangle
     */
    public void resize(Rectangle rectangle) {
        // 如果 宽 比 长 小，进行扩宽操作
        while (rectangle.getWidth() <= rectangle.getLength()) {
            rectangle.setWidth(rectangle.getWidth() + 1);
        }
    }

    /**
     * 打印长和宽
     * @param quadrilateral
     */
    public void print(Quadrilateral quadrilateral) {
        log.info("length={}, width={}", quadrilateral.getLength(), quadrilateral.getWidth());
    }
}
