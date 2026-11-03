package com.hjay.netty.demo.netty.server.session;

import com.hjay.netty.demo.netty.server.session.impl.SessionMemoryImpl;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
