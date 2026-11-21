package com.hjay.sdp.create.factory.prototype;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 表彰/奖状
 *      对象成员
 */
@Data
@Slf4j
public class CitationStu implements Cloneable, Serializable {

    // 学生姓名
    private Student student;

    @Override
    protected CitationStu clone() throws CloneNotSupportedException {
        log.debug(" Citation cloning");
        Student newStu = student.clone();
        CitationStu newCit = (CitationStu) super.clone();
        newCit.setStudent(newStu);
        return newCit;
    }

    public void show(){
        log.info("{}同学，获奖", student.getName());
    }
}
