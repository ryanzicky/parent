package com.zr.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhourui
 * @Date 2022/2/11 17:02
 */
public class Test1 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("nabc");
        list.add("bzzzacfa");
        list.add("habc");
        list.add("bsaacsa");
        list.add("abcasf");
        list.add("dabc");

        list.sort(new CompareTest());
        for (String s : list) {
            System.out.println(s);
        }
        /*List<String> collect = list.stream().sorted(new CompareTest()).collect(Collectors.toList());
        collect.forEach(a -> {
            System.out.println(a);
        });*/
    }
}

class CompareTest implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        String s1 = o1.substring(0, 1);
        String s2 = o2.substring(0, 1);
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        return chars1[0] - chars2[0];
    }
}
