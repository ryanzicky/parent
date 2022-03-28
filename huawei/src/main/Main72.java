package main; /**
 * @Author zhourui
 * @Date 2021/11/15 9:05
 */

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
     *     输入描述：
     *     第一行输入N：N表示有N个小朋友
     *     第二行输入N个小朋友的身高height[i]，都是整数
     *     输出描述：
     *     输出N个小朋友的好朋友的位置
     *
     *     示例一：
     *     输入：
     *         2
     *         100 95
     *     输出：
     *         0 0
     *
     *     说明：
     *     第一个小朋友身高100，站在队尾位置，向队首看，
     *     没有比他身高高的小朋友，所以输出第一个值为0.
     *     第二个小朋友站在队首，前面也没有比他身高高的小朋友，
     *     所以输出第二个值为0.
     *
     *     示例二：
     *     输入
     *         8
     *         123 124 125 121 119 122 126 123
     *     输出
     *         1 2 6 5 5 6 0 0
     *
     *     说明：
     *     123的好朋友是1位置上的124
     *     124的好朋友是2位置上的125
     *     125的好朋友是6位置上的126
     *     以此类推
 */
public class Main72 {

    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            List<Integer> highs = Arrays.stream(in.nextLine().split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < highs.size(); i++) {
                int pos = 0;
                for (int j = i; j < highs.size(); j++) {
                    if (highs.get(j) > highs.get(i)) {
                        pos = j;
                        break;
                    }
                }
                builder.append(pos).append(" ");
            }
            System.out.println(builder.substring(0, builder.length() - 1));
        }
    }
}
