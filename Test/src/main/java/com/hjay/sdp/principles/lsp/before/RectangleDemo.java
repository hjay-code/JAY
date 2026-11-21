package com.hjay.sdp.principles.lsp.before;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试类
 *  里氏替换原则
 *  失败案例
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

        log.info("=========================================");

        // 正方形操作
        Square square = new Square();
        square.setLength(20);
        // 子类重写父类的方法导致死循环
        rectangleDemo.resize(square);
        rectangleDemo.print(square);
    }

    /**
     * 扩宽方法
     * @param rectangle
     */
    public void resize(Rectangle rectangle) {
        // 如果 宽 比 长 小，进行扩宽操作
        // 这个逻辑会因为子类的重写而导致死循环
        while (rectangle.getWidth() <= rectangle.getLength()) {
            rectangle.setWidth(rectangle.getWidth() + 1);
        }
    }

    /**
     * 打印长和宽
     * @param rectangle
     */
    public void print(Rectangle rectangle) {
        log.info("length={}, width={}", rectangle.getLength(), rectangle.getWidth());
    }
}
