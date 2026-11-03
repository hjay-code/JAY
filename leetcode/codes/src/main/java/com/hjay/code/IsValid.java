package com.hjay.code;


import java.util.*;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 *     左括号必须用相同类型的右括号闭合。
 *     左括号必须以正确的顺序闭合。
 *     每个右括号都有一个对应的相同类型的左括号。
 */
public class IsValid {

    public boolean isValid(String s) {
        boolean flag = true;
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        for (int i=0; i<s.length(); i++){
            if (map.containsValue(s.charAt(i))){
                stack.push(s.charAt(i));
            }else if (!stack.isEmpty() && map.get(s.charAt(i)) == stack.peek()){
                stack.pop();
            }else {
                flag = false;
                break;
            }
        }

        if (!stack.isEmpty()){
            flag = false;
        }

        return flag;
    }
}
