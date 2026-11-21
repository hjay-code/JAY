package com.hjay.sdp.principles.lsp.before;

import lombok.NoArgsConstructor;

/**
 * 正方形
 */
@NoArgsConstructor
public class Square extends Rectangle {

    /**
     * 重写设置边长
     * @param length
     */
    @Override
    public void setLength(double length) {
        super.setLength(length);
        super.setWidth(length);
    }

    /**
     * 重写设置宽
     * @param width
     */
    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        super.setLength(width);
    }
}
