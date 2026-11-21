package com.hjay.sdp.principles.lod;

import lombok.extern.slf4j.Slf4j;

/**
 * 迪米特法则
 *  通过agent做中介来让Star，Fans，Company进行通信
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        // 创建经纪人
        Agent agent = new Agent();

        // 创建明星
        Star star = new Star("A");

        // 创建粉丝
        Fans fans = new Fans("B");

        // 创建公司
        Company company = new Company("C");

        // 绑定关系
        agent.setStar(star);
        agent.setFans(fans);
        agent.setCompany(company);

        // 调用功能
        agent.meeting();
        agent.business();
    }
}
