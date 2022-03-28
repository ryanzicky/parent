package test;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2021/11/19 23:38
 */
public class Main1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        String line = scanner.nextLine();
        while (null != line && !line.equals("")) {
            list.add(line);
            line = scanner.nextLine();
        }

        scanner.close();

        ArrayList<String> outList = new ArrayList<>();
        String tmp = "00000000";
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (s.length() <= 8) {
                outList.add(tmp.replace(tmp.substring(0, s.length()), s));
            } else {
                while (s.length() > 8) {
                    s = s.substring(0, 8);
                    outList.add(tmp.replace(tmp.substring(0, s.length()), s));
                }
            }
        }
        for (int i = 0; i < outList.size(); i++) {
            System.out.println(outList.get(i));
        }
    }
}
