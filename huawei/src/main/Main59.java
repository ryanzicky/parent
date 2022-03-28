package main;

/**
 * @Author zhourui
 * @Date 2021/11/24 20:57
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 公司用一个字符串来标识员工的出勤信息
 *
 *     absent:    缺勤
 *     late:      迟到
 *     leaveearly:早退
 *     present:   正常上班
 *
 *     现需根据员工出勤信息,判断本次是否能获得出勤奖,
 *     能获得出勤奖的条件如下：
 *         1.缺勤不超过1次
 *         2.没有连续的迟到/早退
 *         3.任意连续7次考勤 缺勤/迟到/早退 不超过3次
 *
 *      输入描述：
 *       用户的考勤数据字符串记录条数  >=1
 *       输入字符串长度 <10000 ;
 *       不存在非法输入
 *       如：
 *        2
 *        present
 *        present absent present present leaveearly present absent
 *
 *       输出描述：
 *       根据考勤数据字符串
 *       如果能得到考勤奖输出true否则输出false
 *       对于输出示例的结果应为
 *        true false
 *
 *       示例一：
 *        输入：
 *         2
 *         present
 *         present present
 *
 *        输出：
 *         true true
 *
 *       示例二
 *        输入：
 *         2
 *         present
 *         present absent present present leaveearly present absent
 *        输出：
 *         true false
 */
public class Main59 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ArrayList<List<String>> days = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] split = in.nextLine().split(" ");
            List<String> list = Arrays.asList(split);
            days.add(list);
        }
        
        in.close();
        StringBuilder builder = new StringBuilder();
        for (List<String> day : days) {
            if (day.contains("absent")) {
                builder.append("false").append(" ");
                continue;
            }

            boolean flag = true;
        }


    }
}
