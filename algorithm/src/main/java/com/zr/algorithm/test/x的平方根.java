package com.zr.algorithm.test;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author zhourui
 * @Date 2022/3/8 15:15
 */
public class x的平方根 {



    public static void main(String[] args) {
//        System.out.println(newton(25));
        /*String aa = "-349765292140836|1,NY-KYDL-KPB15|1,NY-KYDL-KPB14|1,NY-KYDL-KPB07|1,NY-KYDL-KPB0|1,NY-KYDL-EVAE|1,NY-KYDL-EV10|1,NY-KYDL-EVXE|1,NY-KYDL-XET|1,NY-KYDL-KPL08|1,NY-KYDL-KPL07|1,NY-KYDL-PK805|1,NY-KYDL-PK801|1,NY-KYDL-PK801|2,NY-KYDL-PK801|3,NY-KYDL-PK801|4,NY-KYDL-KPE601|1,NY-KYDL-KPS12|1,NY-KYDL-PK334|1,NY-KYDL-PK334|2,NY-KYDL-PK334|3,NY-KYDL-PK334|4,NY-KYDL-HFE83V|1,NY-KYDL-HFE80V|1,NY-KYDL-SQH|1";
        String[] split = aa.split(",");
        System.out.println(Arrays.asList(split).stream().sorted());
        for (String s : Arrays.asList(split).stream().sorted().collect(Collectors.toList())) {
            System.out.println(s);*/

        /*for (int i = 0; i < 10; i++) {
            int i1 = new Random().nextInt(10);
            System.out.println(i1 % 2);
        }*/
        int[] nums = new int[]{-1,1,1,1,2,1};
        System.out.println(majorityElement(nums));
    }

    public static int majorityElement(int[] nums) {
        Arrays.sort(nums);
        int start = 0, end = 0;
        for (int i = 1; i <= nums.length - 1; i++) {
            if (nums[i] == nums[start]) {
                end++;
            } else {
                start = i + 1;
            }
            if (end - start < nums.length / 2) {
                continue;
            } else {
                break;
            }
        }
        return nums[start];
    }

    public static int binarySearch(int x) {
        int index = -1, l = 0, r = x;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (mid * mid <= x) {
                index = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return index;
    }

    public static int newton(int x) {
        if (x == 0) {
            return 0;
        }
        return (int) sqrt(x, x);
    }

    public static double sqrt(double i, int x) {
       double res = (i + x / i) / 2;
       if (res == i) {
           return i;
       } else {
           return sqrt(res, x);
       }
    }
}
