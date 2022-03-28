package com.zr.algorithm.tx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 贪心算法：会议室排期
 *
 * @Author zhourui
 * @Date 2022/2/21 10:04
 */
public class Meeting implements Comparable<Meeting> {

    int meNum;
    int startTime;
    int endTime;

    public Meeting(int meNum, int startTime, int endTime) {
        this.meNum = meNum;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(@NotNull Meeting o) {
        if (this.endTime > o.endTime) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meNum=" + meNum +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}


class MeetingTest {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<Meeting> meetings = new ArrayList<>();
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            int start = in.nextInt();
            int end = in.nextInt();
            Meeting meeting = new Meeting(i + 1, start, end);
            meetings.add(meeting);
        }

        meetings.sort(null);
        int curTime = 0;
        for (int i = 0; i < n; i++) {
            Meeting meeting = meetings.get(i);
            if (meeting.startTime >= curTime) {
                System.out.println(meeting.toString());
                curTime = meeting.endTime;
            }
        }
    }
}