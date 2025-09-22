package com.ly;

import com.ly.util.SimpleTokenBucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EasyenrollBackApplicationTests {

    public static void main(String[] args) throws InterruptedException {
        //contextLoads();
        String s = "ADOBECODEBANC", t = "ABC";
        minWindow(s, t);


    }

    public static String minWindow(String s, String t) {
        // 思路：维护一个数组，128字符，包含128个ASCII字符，去遍历t，将对应位置--，表示需求量；然后去遍历s，++，若是复数则count++；
        // 需要一个左指针进行收缩，当窗口涵盖所有t的字符时，检查左边是否可以移除（>0）
        // 需要更新最小窗口，有效窗口（count = t的长度）出现时，若当前窗口长度更小则更新。

        // 维护一个数组，用来表示对于t中字符的需求量
        int[] hash = new int[128];
        for (char c : t.toCharArray()) {
            hash[c]--;
        }

        // 维护一个最终返回的子串；s长度；t长度；count t中字符出现次数；最终子串的起始位置；
        String subString = "";
        int sLen = s.length();
        int tLen = t.length();
        int count = 0;
        int subLeft = -1;
        int minLen = sLen + 1;

        // 遍历s串
        for (int i = 0, j = 0; i < sLen; i++) {
            char c = s.charAt(i);
            // 判断是否是t的字符
            if (hash[c] < 0) {
                count++;
            }
            // 更新频率
            hash[c]++;

            // 收缩窗口
            while (j < i && hash[s.charAt(j)] > 0) {
                hash[s.charAt(j)]--;
                j++;
            }

            // 更新最小窗口
            if (count == tLen && i - j + 1 < minLen) {
                minLen = i - j + 1;
                subLeft = j;
            }

        }
        return subLeft >= 0 ? s.substring(subLeft, subLeft + minLen) : "";
    }

    static void contextLoads() throws InterruptedException {
        // 测试令牌桶：容量2，每秒补充1个令牌
        SimpleTokenBucket bucket = new SimpleTokenBucket(2, 1, 1, TimeUnit.SECONDS);

        // 模拟连续请求
        for (int i = 1; i <= 10; i++) {
            System.out.println("请求" + i + ": " +
                    (bucket.tryAcquire() ? "通过" : "被限流"));
            Thread.sleep(300); // 每300ms请求一次
        }
    }

}
