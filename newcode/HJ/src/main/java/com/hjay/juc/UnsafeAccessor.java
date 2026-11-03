package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;


/**
 * @Author: HJAY
 * @Description: 获取Unsafe
 */
@Slf4j(topic = "UnsafeAccessor")
public class UnsafeAccessor {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }
}
