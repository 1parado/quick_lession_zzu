package com.ly.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxValue;
import com.ly.util.VocabUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class CourseLoadService {

    private static final Logger logger = LoggerFactory.getLogger(CourseLoadService.class);
    private static final String MODEL_PATH = "src/main/resources/model/course_load_model.onnx";
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");

    private final OrtEnvironment environment;
    private final OrtSession session;

    public CourseLoadService() throws Exception {
        this.environment = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        this.session = environment.createSession(MODEL_PATH, sessionOptions);
        logger.info("ONNX模型加载成功");
    }

    public PredictResult predictLoad(String content) {
        try {
            logger.debug("预测内容: {}", content);

            // 1. 文本预处理和tokenize
            Map<String, OnnxTensor> inputs = preprocessText(content);

            // 2. 运行模型推理
            OrtSession.Result results = session.run(inputs);

            // 3. 处理输出结果
            Optional<OnnxValue> logitsValue = results.get("logits");
            if (logitsValue.isPresent()) {
                OnnxTensor outputTensor = (OnnxTensor) logitsValue.get();
                float[][] logits = (float[][]) outputTensor.getValue();

                logger.debug("原始logits: {}", Arrays.toString(logits[0]));

                // 4. 计算softmax概率
                float[] probabilities = softmax(logits[0]);
                logger.debug("概率分布: {}", Arrays.toString(probabilities));

                // 5. 找到最高概率的类别
                int predictedClass = 0;
                float maxProb = 0;
                for (int i = 0; i < probabilities.length; i++) {
                    if (probabilities[i] > maxProb) {
                        maxProb = probabilities[i];
                        predictedClass = i;
                    }
                }

                // 将0-4转换回1-5分
                int finalScore = predictedClass + 1;

                logger.info("预测结果: {}分, 置信度: {:.2f}%", finalScore, maxProb * 100);
                return new PredictResult(finalScore, maxProb);
            } else {
                logger.error("模型输出中没有找到logits");
                return new PredictResult(3, 0.5);
            }

        } catch (Exception e) {
            logger.error("课程负载预测失败", e);
            return new PredictResult(3, 0.5);
        }
    }

    private Map<String, OnnxTensor> preprocessText(String text) throws OrtException {
        // 简单的中文tokenization：按字符分割
        List<Integer> tokenIds = new ArrayList<>();

        // 添加CLS token
        tokenIds.add(101); // [CLS]

        // 处理中文字符
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String charStr = String.valueOf(c);

            // 查找字符在vocab中的ID
            Integer tokenId = VocabUtil.getTokenId(charStr);
            if (tokenId != null) {
                tokenIds.add(tokenId);
            } else {
                // 如果字符不在vocab中，使用UNK token
                tokenIds.add(100); // [UNK]
            }

            // 限制最大长度
            if (tokenIds.size() >= 127) {
                break;
            }
        }

        // 添加SEP token
        tokenIds.add(102); // [SEP]

        // 创建input_ids和attention_mask
        long[] inputIds = new long[128];
        long[] attentionMask = new long[128];

        // 填充input_ids
        for (int i = 0; i < tokenIds.size() && i < 128; i++) {
            inputIds[i] = tokenIds.get(i);
            attentionMask[i] = 1L; // 有效token
        }

        // 剩余部分用0填充
        for (int i = tokenIds.size(); i < 128; i++) {
            inputIds[i] = 0L;
            attentionMask[i] = 0L; // 填充token
        }

        logger.debug("input_ids: {}", Arrays.toString(Arrays.copyOf(inputIds, Math.min(10, tokenIds.size()))));
        logger.debug("attention_mask: {}", Arrays.toString(Arrays.copyOf(attentionMask, Math.min(10, tokenIds.size()))));

        long[][] inputIdsArray = {inputIds};
        long[][] attentionMaskArray = {attentionMask};

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", OnnxTensor.createTensor(environment, inputIdsArray));
        inputs.put("attention_mask", OnnxTensor.createTensor(environment, attentionMaskArray));

        return inputs;
    }

    private float[] softmax(float[] logits) {
        float maxLogit = Float.NEGATIVE_INFINITY;
        for (float logit : logits) {
            if (logit > maxLogit) {
                maxLogit = logit;
            }
        }

        float sum = 0;
        float[] exp = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            exp[i] = (float) Math.exp(logits[i] - maxLogit);
            sum += exp[i];
        }

        for (int i = 0; i < exp.length; i++) {
            exp[i] /= sum;
        }
        return exp;
    }

    public record PredictResult(int score, double confidence) {}
}