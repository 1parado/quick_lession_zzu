package com.ly.web;
import com.ly.result.R;
import com.ly.service.BusinessAIService;
import com.ly.service.SelectionService;
import org.springframework.ai.chat.ChatClient; // 注意引入的可能是通用接口
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
public class AiOllamaController {

    @Autowired
    private ChatClient chatClient; // 直接注入通用的 ChatModel

    @Autowired
    private BusinessAIService businessAIService;

    @Autowired
    private SelectionService selectionService;


    public AiOllamaController(ChatClient chatClient,
                              BusinessAIService businessAIService,
                              SelectionService selectionService) {
        this.chatClient = chatClient;
        this.businessAIService = businessAIService;
        this.selectionService = selectionService;
    }

    // 同步响应
    @PostMapping("/sync")
    public R chatSync(@RequestParam String message) {
        try {
            // 智能路由：如果问题是关于选课的，使用业务服务
            if (isCourseSelectionQuestion(message)) {
                String response = businessAIService.answerCourseRelatedQuestion(message);
                return R.success(response);
            } else {
                // 通用问题直接回答
                return R.success(chatClient.call(message));
            }
        } catch (Exception e) {
            return R.error("AI服务暂时不可用: " + e.getMessage());
        }
    }

    /**
     * 直接获取选课统计数据（不经过AI处理）
     */
    @GetMapping("/stats/{courseName}")
    public R getCourseStats(@PathVariable String courseName) {
        try {
            String stats = selectionService.getRelatedCoursesStats(courseName);
            return R.success(stats);
        } catch (Exception e) {
            return R.error("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 判断是否为选课相关业务问题
     */
    private boolean isCourseSelectionQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }

        String lowerQuestion = question.toLowerCase();
        return lowerQuestion.contains("选") && lowerQuestion.contains("课") ||
                lowerQuestion.contains("选择") ||
                lowerQuestion.contains("什么课程") ||
                lowerQuestion.contains("还会") ||
                lowerQuestion.contains("统计") ||
                lowerQuestion.contains("数据");
    }

}
