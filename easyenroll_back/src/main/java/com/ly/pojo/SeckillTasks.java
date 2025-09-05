package com.ly.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * seckill_tasks
 */

public class SeckillTasks implements Serializable {
    private Integer id;

    /**
     * 课程id（只对秒杀课程有效）
     */
    private Integer courseId;

    /**
     * 容量
     */
    private Long stock;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 秒杀状态（未开始、正在抢课中、已结束）
     */
    private Object status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}