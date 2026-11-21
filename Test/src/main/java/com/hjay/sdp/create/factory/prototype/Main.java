package com.hjay.sdp.create.factory.prototype;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 深拷贝
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        // 创建原型
        Relizetype relizetype = new Relizetype();

        // 进行复制
        Relizetype clone = relizetype.clone();

        log.info("原型和复制是否是同一对象:{}",(relizetype == clone));
        log.info("============================================");
        // 获奖对象
        Citation citation = new Citation();
        // 克隆获奖对象
        Citation citation1 = citation.clone();
        citation.setName("张三");
        citation1.setName("李四");
        citation.show();
        citation1.show();

        log.info("============================================");
        CitationStu citationStu = new CitationStu();
        Student student = new Student();
        student.setName("王五");
        citationStu.setStudent(student);

//         如果成员变量是对象，就需要重写 clone 时单独进行成员的深拷贝在重新复制
        CitationStu citationStu1 = citationStu.clone();
        citationStu1.getStudent().setName("赵六");

        // 使用io流进行深拷贝，可以节省嵌套对象的冗余代码
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("CitationStu.txt"));
        oos.writeObject(citationStu);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("CitationStu.txt"));
        CitationStu serilize = (CitationStu) ois.readObject();
        serilize.getStudent().setName("田七");
        ois.close();

        citationStu.show();
        citationStu1.show();
        serilize.show();


    }
}
