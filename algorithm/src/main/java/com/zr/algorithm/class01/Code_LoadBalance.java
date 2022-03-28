package com.zr.algorithm.class01;

import java.util.*;

/**
 * @Author zhourui
 * @Date 2021/9/12 11:05
 */
public class Code_LoadBalance {

    public static final List<String> LIST = Arrays.asList(
            "192.168.0.1",
            "192.168.0.2",
            "192.168.0.3",
            "192.168.0.4",
            "192.168.0.5",
            "192.168.0.6",
            "192.168.0.7",
            "192.168.0.8",
            "192.168.0.9",
            "192.168.0.10"
    );

    private static final Map<String, Integer> WEIGHT_LIST = new HashMap<>();
    static {
        /*权重之和为50*/
        WEIGHT_LIST.put("192.168.0.1", 1);
        WEIGHT_LIST.put("192.168.0.2", 8);
        WEIGHT_LIST.put("192.168.0.3", 3);
        WEIGHT_LIST.put("192.168.0.4", 6);
        WEIGHT_LIST.put("192.168.0.5", 5);
        WEIGHT_LIST.put("192.168.0.6", 5);
        WEIGHT_LIST.put("192.168.0.7", 4);
        WEIGHT_LIST.put("192.168.0.8", 7);
        WEIGHT_LIST.put("192.168.0.9", 2);
        WEIGHT_LIST.put("192.168.0.10", 9);
    }

    /**
     * 随机轮询
     *
     * 当调用次数比较少时，Random 产生的随机数可能会比较集中，
     * 此时多数请求会落到同一台服务器上，只有在经过多次请求后，
     * 才能使调用请求进行“均匀”分配。调用量少这一点并没有什么关系，
     * 负载均衡机制不正是为了应对请求量多的情况吗，所以随机算法也是用得比较多的一种算法。
     *
     * @return
     */
    private static String random() {
        Random random = new Random();
        int randomIps = random.nextInt(LIST.size());
        return LIST.get(randomIps);
    }

    /**
     * 权重随机
     *
     * 那么现在的随机算法应该要改成权重随机算法，
     * 当调用量比较多的时候，服务器使用的分布应该近似对应权重的分布。
     *
     * @return
     */
    public static String weightRandom() {
        List<String> ips = new ArrayList<>();
        for (String ip : WEIGHT_LIST.keySet()) {
            Integer weightIp = WEIGHT_LIST.get(ip);
            for (Integer i = 0; i < weightIp; i++) {
                ips.add(ip);
            }
        }
        Random random = new Random();
        int randomIp = random.nextInt(ips.size());
        return ips.get(randomIp);
    }

    /**
     * 加权随机2
     *
     * @return
     */
    public static String weightRandom2() {
        int totalWeight = 0;
        /*如果所有权重相等，那么随机一个ip就好了*/
        boolean sameWeight = true;
        Object[] weights = WEIGHT_LIST.values().toArray();
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
            for (String ip : WEIGHT_LIST.keySet()) {
                Integer value = WEIGHT_LIST.get(ip);
                if (randomPos < value) {
                    return ip;
                }
                randomPos = randomPos - value;
            }
        }
        return (String) WEIGHT_LIST.keySet().toArray()[new Random().nextInt(WEIGHT_LIST.size())];
    }

    /**
     * 轮询
     *
     * @return
     */
    public static String roundRobin() {
        String ip = "";
        synchronized (pos) {
            if (pos >= LIST.size()) {
                pos = 0;
            }
            ip = LIST.get(pos);
            pos++;
        }
        return ip;
    }

    private static Integer pos = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 11; i++) {
//            System.out.println(random());
//            System.out.println(weightRandom());
//            System.out.println(weightRandom2());
            System.out.println(roundRobin());
        }
    }

    public static class Sequence {
        public static Integer num = 0;
        public static Integer getAndIncrement() {
            return ++num;
        }
    }

    public static class WeightRoundRobin {
        public static Integer pos = 0;
        public static String getServer() {
            int totalWeight = 0;
            boolean sameWeight = true;
            Object[] weights = WEIGHT_LIST.values().toArray();
            for (int i = 0; i < weights.length; i++) {
                Integer weight = (Integer) weights[i];
                totalWeight += weight;
                if (sameWeight && i > 0 && !weight.equals(weights[i - 1])) {
                    sameWeight = false;
                }
            }
            Integer sequenceNum = Sequence.getAndIncrement();
            Integer offset = sequenceNum % totalWeight;
            offset = offset == 0 ? totalWeight : offset;
            if (!sameWeight) {
                for (String ip : WEIGHT_LIST.keySet()) {
                    Integer weight = WEIGHT_LIST.get(ip);
                    if (offset <= weight) {
                        return ip;
                    }
                    offset = offset - weight;
                }
            }
            String ip = null;
            synchronized (pos) {
                if (pos >= LIST.size()) {
                    pos = 0;
                }
                LIST.get(pos);
                pos++;
            }
            return ip;
        }

        public static void main(String[] args) {
            for (int i = 0; i < 11; i++) {
                System.out.println(getServer());
            }
        }
    }


}
