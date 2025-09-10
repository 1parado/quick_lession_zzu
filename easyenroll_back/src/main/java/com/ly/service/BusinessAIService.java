// BusinessAIService.java
package com.ly.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessAIService {

    private final ChatClient chatClient;

    @Autowired
    private SelectionService selectionService;

    public BusinessAIService(ChatClient chatClient, SelectionService selectionService) {
        this.chatClient = chatClient;
        this.selectionService = selectionService;
    }

    /**
     * 处理选课相关的业务问题
     */
    public String answerCourseRelatedQuestion(String userQuestion) {
        // 1. 从用户问题中提取课程名称
        String targetCourse = extractCourseName(userQuestion);

        // 2. 查询数据库获取真实数据
        String realData = selectionService.getRelatedCoursesStats(targetCourse);

        // 3. 构建强大的系统提示词
        String systemPrompt = String.format("""
                你是一个专业的教务选课分析助手。请严格根据以下【真实选课统计数据】来回答问题。
                你的任务是基于这些真实数据进行分析和总结，用专业、友好的语气回复用户。
                
                重要规则：
                1. 必须基于提供的数据回答，不能编造不存在的信息
                2. 分析选择模式并给出合理的解释
                3. 回复要简洁明了，突出重点
                
                【真实选课统计数据】：
                %s
                """, realData);

        // 4. 组合系统指令和用户问题
        String fullPrompt = systemPrompt + "\n\n用户问题：" + userQuestion;

        return chatClient.call(fullPrompt);
    }

    /**
     * 从用户问题中提取课程名称
     */
    private String extractCourseName(String question) {
        // 优先提取引号内的内容
        if (question.contains("「") && question.contains("」")) {
            return question.substring(question.indexOf("「") + 1, question.indexOf("」"));
        } else if (question.contains("《") && question.contains("》")) {
            return question.substring(question.indexOf("《") + 1, question.indexOf("》"));
        } else if (question.contains("\"") && question.lastIndexOf("\"") > question.indexOf("\"")) {
            return question.substring(question.indexOf("\"") + 1, question.lastIndexOf("\""));
        } else if (question.contains("'") && question.lastIndexOf("'") > question.indexOf("'")) {
            return question.substring(question.indexOf("'") + 1, question.lastIndexOf("'"));
        }

        // 尝试匹配常见课程名称
        String[] commonCourses = {
                "数据结构", "操作系统", "计算机网络", "数据库原理",
                "面向对象", "软件工程", "计算机组成原理", "算法分析",
                "编译原理", "离散数学", "高等数学", "线性代数"
        };

        for (String course : commonCourses) {
            if (question.contains(course)) {
                return course;
            }
        }

        // 默认返回"数据结构"
        return "数据结构";
    }
}