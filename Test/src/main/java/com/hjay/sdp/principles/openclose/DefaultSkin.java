package com.hjay.sdp.principles.openclose;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认皮肤实现类
 */
@Slf4j
public class DefaultSkin extends AbstractSkin{
    @Override
    public void display() {
        log.info("默认皮肤");
    }
}
