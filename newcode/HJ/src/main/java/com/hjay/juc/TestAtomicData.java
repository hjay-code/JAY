package com.hjay.juc;


import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 使用自定义的原子类实现线程安全的计数器
 */
@Slf4j(topic = "TestAtomicData")
public class TestAtomicData {
}

@Slf4j(topic = "AtomicData")
class AtomicData {

    private int value;
    private static final long VALUE_OFFSET;
    private static final Unsafe UNSAFE;
    static {
        UNSAFE = UnsafeAccessor.getUnsafe() ;
        try {
            VALUE_OFFSET = UNSAFE.objectFieldOffset(AtomicData.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            log.error("获取字段失败");
            throw new RuntimeException(e);
        }
    }


    public int getValue() {
        return value;
    }

    public void decrement(int amount){
        while (true){
            int prev = this.value;
            int next = prev = amount;
            if (UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, prev, next)) {
                break;
            }
        }
    }

    public AtomicData(int value){
        this.value = value;
    }

}
