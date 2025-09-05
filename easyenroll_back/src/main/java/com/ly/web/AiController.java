package com.ly.web;

import com.ly.result.R;
import com.ly.service.CourseLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private CourseLoadService courseLoadService;

    @PostMapping("/predict-course-load")
    public R<CourseLoadService.PredictResult> predictCourseLoad(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            if (content == null || content.trim().isEmpty()) {
                return (R<CourseLoadService.PredictResult>) R.error("错误");
            }
            CourseLoadService.PredictResult result = courseLoadService.predictLoad(content);
            return R.success(result);
        } catch (Exception e) {
            return (R<CourseLoadService.PredictResult>) R.error("异常");
        }
    }
}
