package com.zr.algorithm.class01;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author zhourui
 * @Date 2021/9/12 16:33
 */
public class ConsistentHash {

    private static SortedMap<Integer, String> virtualNodes = new TreeMap<>();
    private static final int VIRTUAL_NODES = 160;
    static {
        /*对每一个真实节点添加虚拟节点，虚拟节点会根据哈希算法进行散列*/
        for (String ip : Code_LoadBalance.LIST) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                int hash = getHash(ip + "VN" + i);
                virtualNodes.put(hash, ip);
            }
        }
    }

    private static int getHash(String s) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash ^ s.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    public static String getServer(String client) {
        int hash = getHash(client);
        /*得到大于该hash值的排好序的Map*/
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        /*大于该hash值的元素，则返回根节点*/
        Integer nodeIndex = subMap.firstKey();
        /*如果不存在大于该hash值的元素，则返回根节点*/
        if (nodeIndex == null) {
            nodeIndex = virtualNodes.firstKey();
        }
        /*返回对应的虚拟节点名称*/
        return subMap.get(nodeIndex);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 11; i++) {
            System.out.println(getServer("client" + i));
        }
    }
}
