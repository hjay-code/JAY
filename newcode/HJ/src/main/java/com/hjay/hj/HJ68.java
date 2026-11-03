package com.hjay.hj;


import java.util.Arrays;
import java.util.Scanner;

/**
 * 描述
 * 对于给出的 nn 条姓名和成绩信息，根据指定的排序方式按成绩升序或降序排列并输出。
 * 特别的，成绩相同的同学需要保持输入的先后顺序进行排序。可能存在多条信息的学生姓名一致。
 * 输入描述：
 * 第一行输入一个整数 n(1≦n≦200)n(1≦n≦200) 代表学生人数。
 * 第二行输入一个整数 op(0≦op≦1)op(0≦op≦1) 代表排序方式，其中，00 表示按成绩降序，11 表示按成绩升序。
 * 此后 nn 行，第 ii 行依次输入：
 * ∙ ∙一个长度为 1≦len(si)≦201≦len(si​)≦20、由大小写字母构成的字符串 sisi​ 代表第 ii 个学生的姓名；
 * ∙ ∙一个整数 ai(0≦ai≦100)ai​(0≦ai​≦100) 代表这个学生的成绩。
 * 输出描述：
 * 根据输入的排序方式，按照成绩升序或降序输出所有学生的姓名和成绩。对于每一名学生，新起一行。输出学生的姓名和成绩，用空格分隔。
 */
public class HJ68 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int op = in.nextInt();
            Student[] students = new Student[n];
            for (int i = 0; i < n; i++) {
                students[i] = new Student(in.next(), in.nextInt());
                students[i].setIndex(i);
                students[i].setOp(op);
            }
            Arrays.sort(students);
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
}

class Student  implements Comparable<Student>{
    private String name;
    private int score;
    private int index;
    private int op;
    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public Student() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getOp() {
        return op;
    }
    public void setOp(int op) {
        this.op = op;
    }
    @Override
    public String toString() {
        return name + " " + score;
    }

    @Override
    public int compareTo(Student o) {
        if (this.getOp() == 0) {
            if (this.getScore() == o.getScore()) {
                return this.getIndex() - o.getIndex();
            }
            return o.getScore() - this.getScore();
        }else {
            if (this.getScore() == o.getScore()) {
                return this.getIndex() - o.getIndex();
            }
            return this.getScore() - o.getScore();
        }
    }
}
