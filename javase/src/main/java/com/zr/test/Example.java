package com.zr.test;

import org.assertj.core.util.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhourui
 * @Date 2021/6/17 11:46
 */
public class Example {

    String str = new String("good");
    char[] ch = {'a', 'b', 'c'};

    public static void main(String[] args) {
//        System.out.println(formatData(new Date()));

        System.out.println(getDifferent(Lists.newArrayList(1L, 2L, 3L), Lists.newArrayList(1L, 3L)));
    }

    public static Date getDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String formatData(Date date) {
        return FORMAT.format(date);
    }

    /**
     * 获取差异id列表
     *
     * @param oldIds
     * @param newIds
     * @return
     */
    private static List<Long> getDifferent(List<Long> oldIds, List<Long> newIds) {
        return oldIds.parallelStream().filter(x -> !newIds.contains(x)).collect(Collectors.toList());
    }
}
