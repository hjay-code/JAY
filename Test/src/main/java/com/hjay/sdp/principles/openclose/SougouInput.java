package com.hjay.sdp.principles.openclose;

import lombok.Data;

/**
 * 输入法工具
 */
@Data
public class SougouInput {

    private AbstractSkin skin;

    public void display() {
        this.skin.display();
    }
}
