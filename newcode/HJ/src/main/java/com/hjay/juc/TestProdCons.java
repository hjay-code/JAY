package com.hjay.juc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 生产者消费者模型
 */
@Slf4j
public class TestProdCons {

    public static void main(String[] args) {

        MessageQueue messageQueue = new MessageQueue(2);

        for (int i = 0; i < 30; i++){
            int id = i;
            new Thread(() -> {
                Message message = new Message(id, "message:" + id);
                log.info("生产了：{}", message);
                messageQueue.put(message);
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while ( true){
                Sleeper.sleep(1);
                Message take = messageQueue.take();
                log.info("消费了：{}", take);
            }
        }, "消费者").start();

    }
}

/**
 * 消息队列
 */
@Slf4j(topic = "MessageQueue")
class MessageQueue {

    private final LinkedList<Message> queue = new LinkedList<>();
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    log.info("队列为空，等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.removeFirst();
            queue.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() == capacity) {
                try {
                    log.info("队列已满，等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(message);
            queue.notifyAll();
        }
    }

}

@Getter
@Slf4j(topic = "Message")
final class Message {

    private final int id;
    private final Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
