package com.zr.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/13 10:51
 */
public class LeetCode_22 {

    public static void main(String[] args) {
        LeetCode_22 leetCode_22 = new LeetCode_22();
        leetCode_22.generateParenthesis(3);
    }

    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        generateOneByOne("", list, n, n);
        return list;
    }

    private void generateOneByOne(String sublist, List<String> list, int left, int right) {
        if (left == 0 && right == 0) {
            list.add(sublist);
            return;
        }

        if (left > 0) {
            generateOneByOne(sublist + "(", list, left - 1, right);
        }
        if (right > left) {
            generateOneByOne(sublist + ")", list, left, right - 1);
        }
    }
}
