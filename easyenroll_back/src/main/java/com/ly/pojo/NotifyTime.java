package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * notify_time
 */
@Data
public class NotifyTime implements Serializable {
    private Integer id;

    /**
     * 草稿箱公告id
     */
    private Integer draftId;

    /**
     * 默认为0不提前发送短信，单位为分钟
     */
    private Integer time;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDraftId() {
        return draftId;
    }

    public void setDraftId(Integer draftId) {
        this.draftId = draftId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotifyTime that = (NotifyTime) o;
        return Objects.equals(id, that.id) && Objects.equals(draftId, that.draftId) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, draftId, time);
    }

    @Override
    public String toString() {
        return "NotifyTime{" +
                "id=" + id +
                ", draftId=" + draftId +
                ", time=" + time +
                '}';
    }
}