package com.hjay.code;

import java.util.HashSet;
import java.util.Set;

/**
 *
 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
 然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
 如果这个过程 结果为 1，那么这个数就是快乐数。
 如果 n 是 快乐数 就返回 true ；不是，则返回 false 。
 */
public class IsHappy {

    /**
     * 我的思路：和官方题解一样，使用hash存储，判断是否进入无限循环
     * @param n
     * @return
     */
    public boolean isHappy(int n) {
        Set<Integer> set = new HashSet<>();
        while (n != 1){
            set.add(n);
            n = getNum(n);
            if (set.contains(n)) return false;
        }

        return true;
    }

    public int getNum(int n){
        if (n == 0) return n;
        int num = n % 10;
        return num * num + getNum(n/10);
    }


    /**
     * 大神优化：使用快慢指针
     * 没有使用set，简化判断
     * @param n
     * @return
     */
    public boolean isHappy_leetcode(int n) {
        int slow = n;
        int fast = n;
        do {
            slow = getNum(slow);
            fast = getNum(fast);
            fast = getNum(fast);
        }while (slow != fast);

        return slow == 1;
    }
}
