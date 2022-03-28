package com.zr.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author zhourui
 * @Date 2021/8/10 11:12
 */
public class Test {

    private static String PATTERN = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}-\\d{1,2}:\\d{1,2}";

    public static void main(String[] args) {
        String aa = "2021-12-12 2:00-3:00";
        String bb = "2021-12-12 1:12-2:32";
        System.out.println(Pattern.matches(PATTERN, aa));
        System.out.println(Pattern.matches(PATTERN, bb));

        String[] s = aa.split("-");
        Arrays.asList(s).stream().forEach(x -> System.out.println(x));
        String[] split = s[1].split("-");
        String[] split1 = split[0].split(":");
        System.out.println(split1[0]);
        String[] split2 = split[1].split(":");
        System.out.println(split2[0]);

    }

    public static Date getDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date getDateAddHours(Date date, int hours) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
