package com.zr.algorithm.class01;

import java.util.*;

/**
 * @Author zhourui
 * @Date 2021/9/12 16:46
 */
public class LeastActive {

    private static String getServer() {
        /*找出当前活跃数最小的服务器*/
        Optional<Integer> minValue = ServerIps.ACTIVITY_LIST.values().stream().min(Comparator.naturalOrder());
        if (minValue.isPresent()) {
            ArrayList<String> minActivityIps = new ArrayList<>();
            ServerIps.ACTIVITY_LIST.forEach((ip, activity) -> {
                if (activity.equals(minValue.get())) {
                    minActivityIps.add(ip);
                }
            });

            /*最小活跃数的ip有多个，则根据权重来选，权重大的优先*/
            if (minActivityIps.size() > 1) {
                /*过滤出对应的ip和权重*/
                LinkedHashMap<String, Integer> weightList = new LinkedHashMap<>();
                ServerIps.WEIGHT_LIST.forEach((ip, weight) -> {
                    if (minActivityIps.contains(ip)) {
                        weightList.put(ip, ServerIps.WEIGHT_LIST.get(ip));
                    }
                });

                int totalWeight = 0;
                boolean sameWeight = true;

                Object[] weights = weightList.values().toArray();
                for (int i = 0; i < weights.length; i++) {
                    Integer weight = (Integer) weights[i];
                    totalWeight += weight;
                    if (sameWeight && i > 0 && !weight.equals(weights[i - 1])) {
                        sameWeight = false;
                    }
                }
                Random random = new Random();
                int randomPos = random.nextInt(totalWeight);
                if (!sameWeight) {
                    for (String ip : weightList.keySet()) {
                        Integer value = weightList.get(ip);
                        if (randomPos < value) {
                            return ip;
                        }
                        randomPos = randomPos - value;
                    }
                }
                return (String) weightList.keySet().toArray()[new Random().nextInt(weightList.size())];
            } else {
                return minActivityIps.get(0);
            }
        } else {
            return (String) ServerIps.WEIGHT_LIST.keySet().toArray()[new Random().nextInt(ServerIps.WEIGHT_LIST.size())];
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 11; i++) {
            System.out.println(getServer());
        }
    }
}
