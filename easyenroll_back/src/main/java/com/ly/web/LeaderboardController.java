package com.ly.web;

import com.ly.annotation.RoleRequire;
import com.ly.dto.TopDataOneDTO;
import com.ly.mapper.StudentsMapper;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private StudentsMapper studentsMapper;

    /**
     * 获取排行榜数据（TopN）
     * @param n
     * @return
     */
    @GetMapping("/leaderboard/topN/{n}")
    @RoleRequire("STUDENT")
    public R getTopN(@PathVariable int n){
        try {
            Set<ZSetOperations.TypedTuple<String>> topN = leaderboardService.getTopN(n);
            // 转换为包含更多信息（如姓名）的列表
            List<Map<String, Object>> result = new ArrayList<>();
            for (ZSetOperations.TypedTuple<String> tuple : topN) {
                String sno = tuple.getValue();
                Double score = tuple.getScore();

                // 根据学号查询学生信息（需要实现此方法）
                Students student = studentsMapper.selectBySno(Long.valueOf(sno));

                Map<String, Object> item = new HashMap<>();
                item.put("value", sno);
                item.put("score", score);
                item.put("name", student != null ? student.getName() : "未知");

                result.add(item);
            }

            return R.success(result);
        } catch (Exception e) {
            return R.error("获取排行榜失败");
        }
    }

    @GetMapping("/leaderboard/{sno}")
    @RoleRequire("STUDENT")
    public R getOne(@PathVariable Long sno){
        try {
            long userRank = leaderboardService.getUserRank(String.valueOf(sno));
            Double userScore = leaderboardService.getUserScore(String.valueOf(sno));
            TopDataOneDTO topDataOneDTO = new TopDataOneDTO();
            topDataOneDTO.setRank(userRank);
            topDataOneDTO.setScore(userScore);
            return R.success(topDataOneDTO);
        } catch (Exception e) {
            return R.error("获取排行榜失败");
        }
    }
}
