package com.hjay.juc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

/**
 * @Author: HJAY
 * 十分底层，不推荐使用
 * 操作底层内存和线程
 */
@Slf4j(topic = "TestUnsafe")
public class TestUnsafe {

    public static void main(String[] args) throws Exception {

//        test1();

        test2();
    }

    /**
     * jdk9的操作
     * @throws Exception
     */
    public static void test2() throws Exception {
        VarHandle nameHandle = MethodHandles.lookup()
                .findVarHandle(Teacher.class, "name", String.class);
        VarHandle idHandle = MethodHandles.lookup()
                .findVarHandle(Teacher.class, "id", int.class);

        Teacher t = new Teacher();

        // CAS 操作
        boolean casIdSuccess = idHandle.compareAndSet(t, 0, 1);
        boolean casNameSuccess = nameHandle.compareAndSet(t, null, "HJAY");

        log.info("CAS id success: {}, CAS name success: {}", casIdSuccess, casNameSuccess);
        log.info("name: {}, id: {}", t.getName(), t.getId());
    }


    /**
     * jdk8的操作
     * @throws Exception
     */
    public static void test1() throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);// 允许访问私有成员变量
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        // 1.获取域的偏移地址
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));

        Teacher t = new Teacher();
        // 2.执行cas操作
        unsafe.compareAndSwapInt(t, idOffset, 0, 1);
        unsafe.compareAndSwapObject(t, nameOffset, null, "HJAY");

        // 3.查看结果
        log.info("name: {}, id: {}", t.getName(), t.getId());
    }
}


@Slf4j(topic = "Teacher")
@Data
class Teacher{
    volatile String name;
    volatile int id;
}
