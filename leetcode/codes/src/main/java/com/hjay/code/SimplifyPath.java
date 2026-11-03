package com.hjay.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 给你一个字符串 path ，表示指向某一文件或目录的 Unix 风格 绝对路径 （以 '/' 开头），请你将其转化为更加简洁的规范路径。
在 Unix 风格的文件系统中，一个点（.）表示当前目录本身；此外，两个点 （..） 表示将目录切换到上一级（指向父目录）；两者都可以是复杂相对路径的组成部分。
任意多个连续的斜杠（即，'//'）都被视为单个斜杠 '/' 。 对于此问题，任何其他格式的点（例如，'...'）均被视为文件/目录名称。

请注意，返回的 规范路径 必须遵循下述格式：

    始终以斜杠 '/' 开头。
    两个目录名之间必须只有一个斜杠 '/' 。
    最后一个目录名（如果存在）不能 以 '/' 结尾。
    此外，路径仅包含从根目录到目标文件或目录的路径上的目录（即，不含 '.' 或 '..'）。

返回简化后得到的 规范路径 。
 */
public class SimplifyPath {
	
	public static void main(String[] args) {
		String s = "/a/./b/../../c/";
		String s1 = "/.../a/../b/c/../d/./";
		String s2 = "/a/../../b/../c//.//";
		String s3 = "/../";
		System.out.println(new SimplifyPath().simplifyPath(s3));;
	}

	public String simplifyPath(String path) {
		String[] paths = path.split("/");
		String s = null;
		List<String> list = new ArrayList<>();
		int index = 0;
		for (int i=0 ;i< paths.length; i++) {
			s = paths[i];
			if (s.equals(".") || s.length() == 0) {
				continue;
			} else if (s.equals("..")) {
				if (!list.isEmpty()) {
					if(index < 1) {
						list.set(0, "");
					}else {
						index --;
						list.set(index, "");
					}
				}
				
			}else {
				list.add(index,"/" +s);
				index ++;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (String l : list) {
			sb.append(l);
		}
		if(sb.length() == 0) sb.append("/");
		return sb.toString();
    }
	
	/**
	 * 官方题解
	 * 和之前的队列思路一致，不过我不知道弹出的用法
	 * @param path
	 * @return
	 */
	public String simplifyPath_leetcode(String path) {
        String[] names = path.split("/");
        Deque<String> stack = new ArrayDeque<String>();
        for (String name : names) {
            if ("..".equals(name)) {
                if (!stack.isEmpty()) {
                    stack.pollLast();
                }
            } else if (name.length() > 0 && !".".equals(name)) {
                stack.offerLast(name);
            }
        }
        StringBuffer ans = new StringBuffer();
        if (stack.isEmpty()) {
            ans.append('/');
        } else {
            while (!stack.isEmpty()) {
                ans.append('/');
                ans.append(stack.pollFirst());
            }
        }
        return ans.toString();
    }
}
