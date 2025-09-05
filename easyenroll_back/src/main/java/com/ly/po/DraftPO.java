package com.ly.po;

import java.util.Date;
import java.util.Objects;

public class DraftPO {
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 发布人
     */
    private String publicName;

    /**
     * 发布状态（已发布、草稿、已删除）
     */
    private Object status;

    /**
     * 提前发布时间
     */
    private int time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DraftPO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publishTime=" + publishTime +
                ", publicName='" + publicName + '\'' +
                ", status=" + status +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DraftPO draftPO = (DraftPO) o;
        return time == draftPO.time && Objects.equals(id, draftPO.id) && Objects.equals(title, draftPO.title) && Objects.equals(content, draftPO.content) && Objects.equals(publishTime, draftPO.publishTime) && Objects.equals(publicName, draftPO.publicName) && Objects.equals(status, draftPO.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, publishTime, publicName, status, time);
    }
}
