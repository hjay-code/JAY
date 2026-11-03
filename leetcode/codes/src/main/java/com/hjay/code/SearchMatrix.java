package com.hjay.leetcode;

/**
 * 给你一个满足下述两条属性的 m x n 整数矩阵：
 *  每行中的整数从左到右按非严格递增顺序排列。
 *  每行的第一个整数大于前一行的最后一个整数。
 * 给你一个整数 target ，如果 target 在矩阵中，返回 true ；否则，返回 false 。
 */
public class SearchMatrix {

    public boolean searchMatrix(int[][] matrix, int target) {
        boolean flag = false;
        int m = matrix.length;
        int n = matrix[0].length;
        int min = matrix[0][0];
        int max = matrix[m-1][n-1];
        if (target < min || target > max) return flag;
        if (target == min || target == max) return true;
        for (int i=0; i<m;i++){
            if (target>=matrix[i][0] && target <= matrix[i][n-1]){
                for (int j=0; j<n;j++){
                    if (target == matrix[i][j]){
                        flag = true;
                        break;
                    }
                }
                break;
            }
        }

        return flag;
    }

    /**
     * 官方题解2
     * 相当于把每一行都拼起来，当成一个一维数组，然后进行二分查找
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix_leetcode2(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int low = 0, high = m * n - 1;
        while (low <= high) {
            int mid = (high - low) / 2 + low;
            int x = matrix[mid / n][mid % n];
            if (x < target) {
                low = mid + 1;
            } else if (x > target) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 官方题解1
     * 找到target在哪一行，然后进行行内的二分查找
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix_leetcode(int[][] matrix, int target) {
        int rowIndex = binarySearchFirstColumn(matrix, target);
        if (rowIndex < 0) {
            return false;
        }
        return binarySearchRow(matrix[rowIndex], target);
    }

    public int binarySearchFirstColumn(int[][] matrix, int target) {
        int low = -1, high = matrix.length - 1;
        while (low < high) {
            int mid = (high - low + 1) / 2 + low;
            if (matrix[mid][0] <= target) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    public boolean binarySearchRow(int[] row, int target) {
        int low = 0, high = row.length - 1;
        while (low <= high) {
            int mid = (high - low) / 2 + low;
            if (row[mid] == target) {
                return true;
            } else if (row[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;
    }

}
