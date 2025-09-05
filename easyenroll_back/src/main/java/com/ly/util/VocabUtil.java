// VocabUtil.java
package com.ly.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class VocabUtil {

    private static final Logger logger = LoggerFactory.getLogger(VocabUtil.class);
    private static final Map<String, Integer> VOCAB_MAP = new HashMap<>();
    private static final Map<Integer, String> ID_TO_TOKEN_MAP = new HashMap<>();

    static {
        loadVocab();
    }

    private static void loadVocab() {
        try {
            ClassPathResource resource = new ClassPathResource("model/vocab.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                VOCAB_MAP.put(line.trim(), index);
                ID_TO_TOKEN_MAP.put(index, line.trim());
                index++;
            }
            reader.close();
            logger.info("成功加载词汇表，共 {} 个token", VOCAB_MAP.size());
        } catch (Exception e) {
            logger.error("加载词汇表失败", e);
        }
    }

    public static Integer getTokenId(String token) {
        return VOCAB_MAP.get(token);
    }

    public static String getTokenById(int id) {
        return ID_TO_TOKEN_MAP.get(id);
    }

    public static boolean containsToken(String token) {
        return VOCAB_MAP.containsKey(token);
    }

    public static int getVocabSize() {
        return VOCAB_MAP.size();
    }
}