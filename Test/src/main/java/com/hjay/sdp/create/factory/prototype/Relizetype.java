package com.hjay.sdp.create.factory.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * 深拷贝实现
 */
@Slf4j
public class Relizetype implements Cloneable {

    public Relizetype() {
        log.info("Relizetype constructor");
    }


    @Override
    protected Relizetype clone() throws CloneNotSupportedException {
        log.info("Relizetype clone method");
        return (Relizetype ) super.clone();
    }
}
