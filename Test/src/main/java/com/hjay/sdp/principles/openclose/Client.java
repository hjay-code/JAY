package com.hjay.sdp.principles.openclose;


/**
 * 客户端
 *  开闭原则实现
 */
public class Client {

    public static void main(String[] args) {

        // 输入法
        SougouInput sougouInput = new SougouInput();
        // 创建默认皮肤对象
        DefaultSkin defaultSkin = new DefaultSkin();
        // 设置默认皮肤
        sougouInput.setSkin(defaultSkin);
        // 展示皮肤
        sougouInput.display();

        // 创建自定义皮肤
        JaySkin jaySkin = new JaySkin();
        sougouInput.setSkin(jaySkin);
        sougouInput.display();
    }
}
