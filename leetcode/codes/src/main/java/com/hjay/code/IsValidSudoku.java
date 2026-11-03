package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。

    数字 1-9 在每一行只能出现一次。
    数字 1-9 在每一列只能出现一次。
    数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
    
    注意：

    一个有效的数独（部分已被填充）不一定是可解的。
    只需要根据以上规则，验证已经填入的数字是否有效即可。
    空白格用 '.' 表示。


 */
public class IsValidSudoku {
	
	/**
	 * 
	 * @param board
	 * @return
	 */
	public boolean isValidSudoku(char[][] board) {
		
		Map<Character, List<Position>> number = new HashMap<>();
		number.put('1', new ArrayList<Position>());
		number.put('2', new ArrayList<Position>());
		number.put('3', new ArrayList<Position>());
		number.put('4', new ArrayList<Position>());
		number.put('5', new ArrayList<Position>());
		number.put('6', new ArrayList<Position>());
		number.put('7', new ArrayList<Position>());
		number.put('8', new ArrayList<Position>());
		number.put('9', new ArrayList<Position>());
		
		char a= '.';
		List<Position> list = null;
		Position p = null;
		for(int i=0; i< board.length; i++) {
			for(int j=0; j<board.length; j++) {
				a = board[i][j];
				if (a == '.') continue;
				list = number.get(a);
				p = new Position(i, j);
				if(list.size() == 0) {
					list.add(p);
				}else {
					if(!judgePosition(p, list)) {
						return false;
					}else {
						list.add(p);
					}
				}
			}
		}
		
		return true;
    }
	
	
	public boolean judgePosition(Position p, List<Position> list) {
	
		for (Position i: list) {
			if(!p.judge(i)) return false;
		}
		return true;
	}

}

/**
 * 用来存放坐标
 */
class Position{
	public int x;
	public int y;
	public int no;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.no = x/3 * 10 + y/3;
	}
	
	public boolean judge(Position p) {
		
		if (p.x == this.x) return false;
		if (p.y == this.y) return false;
		if (p.no == this.no) return false;
		
		return true;
		
	}
}
