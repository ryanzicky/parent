package test;


import java.util.*;

/**
 * @Author zhourui
 * @Date 2021/11/21 14:53
 */
public class Main4 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            int num = in.nextInt();
            String [] arr = new String[num];
            for (int i = 0; i < num; i++) {
                arr[i] = in.nextLine();
            }

            Arrays.sort(arr);
            for (String s : arr) {
                System.out.println(s);
            }
        }
        in.close();
    }
}
