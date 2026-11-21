package com.hjay.sdp.principles.dip.after;

/**
 * 硬盘接口
 */
public interface HardDisk {

    void save(String data);

    String get();
}
