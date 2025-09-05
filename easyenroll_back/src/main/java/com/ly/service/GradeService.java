package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.CourseGradePO;

public interface GradeService {
    PageInfo<CourseGradePO> getGradePOBySno(Long sno, int page, int size);

    int check(Long no, String code);
}
