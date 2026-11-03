package com.hjay.netty.demo.netty.server.session;

import com.hjay.netty.demo.netty.server.session.impl.GroupSessionMemoryImpl;

public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
