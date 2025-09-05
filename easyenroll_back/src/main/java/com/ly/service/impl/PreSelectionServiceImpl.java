package com.ly.service.impl;

import com.ly.dto.PreSelectionDTO;
import com.ly.mapper.PreSelectionMapper;
import com.ly.pojo.PreSelection;
import com.ly.service.PreSelectionService;
import com.ly.service.RedisService;
import com.ly.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PreSelectionServiceImpl implements PreSelectionService {

    @Autowired
    private PreSelectionMapper preSelectionMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public int savePreSelection(PreSelectionDTO preData) {
        List<String> courseCodes = preData.getCourseCodes();
        int studentId = preData.getStudentId();
        //数据库中，根据学生id删除记录后，再进行插入
        preSelectionMapper.deleteByStudentId(studentId);
        for (String courseCode : courseCodes) {
            PreSelection preSelection = new PreSelection();
            preSelection.setCourseCode(courseCode);
            preSelection.setStudentId(studentId);
            preSelectionMapper.insert(preSelection);
        }

        //保存在Redis
        String key = Constant.PREDATA_PREFIX + studentId;
        redisService.removeValue(key);
        redisService.setValue(key, courseCodes);
        return 1;
    }
}
