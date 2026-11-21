package com.hjay.sdp.principles.lod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 经纪人
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {

    private Star star;
    private Fans fans;
    private Company company;


    /**
     * 和粉丝见面
     */
    public void meeting() {
        log.info("{} metting {}", star.getName(), fans.getName());
    }

    /**
     * 和公司洽谈
     */
    public void business(){
        log.info("{} business {}", star.getName(), company.getName());
    }
}
