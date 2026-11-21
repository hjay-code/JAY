package com.hjay.sdp.principles.openclose;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义皮肤实现类
 */
@Slf4j
public class JaySkin extends AbstractSkin{
    @Override
    public void display() {
        log.info("Jay 自定义皮肤");
    }
}
