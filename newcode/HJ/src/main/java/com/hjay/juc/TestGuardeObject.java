package com.hjay.juc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TestGuardeObject {

    public static void main(String[] args) {
//        guardeObject();
        guardeObject2();
    }


    /**
     * 保护性暂停
     */
    public static void guardeObject() {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            log.info("等待结果");
//            Object response = guardedObject.get();
            Object response = guardedObject.get(2000);
            log.info("结果：{}", response);
        }, "T1").start();
        new Thread(() -> {
            log.info("执行计算");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardedObject.complete("ok");
        }, "T2").start();
    }


    /**
     * 保护性暂停，解耦等待
     * 主要用于rpc等框架中，一对一的模式
     */
    public static void guardeObject2() {
        for (int i = 0; i < 5; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id: Mailboxes.getIds()){
            new Postman(id, "mail" + id).start();
        }

    }
}


@Slf4j(topic = "People")
class People extends Thread {

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.info("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get();
        log.info("收到信 id:{}, mail:{}", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "Postman")
class Postman extends Thread{

    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        log.info("开始发送 id:{} mail:{}", id, mail);
        Mailboxes.getGuardedObject(id).complete(mail);
        log.info("发送完成 id:{} mail:{}", id, mail);
    }
}


@Slf4j(topic = "Mailboxes")
class Mailboxes {
    private static final Map<Integer, GuardedObject> mailboxes = new Hashtable<>();

    private static int id = 1;

    public static synchronized int grneateId() {
        return id++;
    }
    public static GuardedObject getGuardedObject(int id) {
        return mailboxes.remove(id);
    }
    public static GuardedObject createGuardedObject() {
        GuardedObject guardedObject = new GuardedObject(grneateId());
        mailboxes.put(guardedObject.getId(), guardedObject);
        return guardedObject;
    }

    public static Set<Integer> getIds() {
        return mailboxes.keySet();
    }
}

@Slf4j(topic = "GuardedObject")
class GuardedObject {
    // 标识
    @Getter
    private int id;
    private Object response;

    public GuardedObject() {
        this.id = Mailboxes.grneateId();
    }

    public GuardedObject(int id) {
        this.id = id;
    }

    public Object get() {
        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    log.info("被中断");
                }
            }
            return response;
        }
    }

    /**
     * 超时等待
     * @param timeout
     * @return
     */
    public Object get(long timeout) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            long millis = timeout - (System.currentTimeMillis() - now);
            while (response == null && millis > 0) {
                try {
                    this.wait(millis);
                } catch (InterruptedException e) {
                    log.info("被中断");
                }
                millis = timeout - (System.currentTimeMillis() - now);
            }
            return response;
        }
    }
    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }

}
