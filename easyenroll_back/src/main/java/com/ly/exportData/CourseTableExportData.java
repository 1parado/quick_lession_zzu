package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

public class CourseTableExportData {
    @ExcelProperty("时间")
    private String time;

    @ExcelProperty("周一")
    private String monday;

    @ExcelProperty("周二")
    private String tuesday;

    @ExcelProperty("周三")
    private String wednesday;

    @ExcelProperty("周四")
    private String thursday;

    @ExcelProperty("周五")
    private String friday;

    @ExcelProperty("周六")
    private String saturday;

    @ExcelProperty("周日")
    private String sunday;

    @Override
    public String toString() {
        return "CourseTableExportData{" +
                "time='" + time + '\'' +
                ", monday='" + monday + '\'' +
                ", tuesday='" + tuesday + '\'' +
                ", wednesday='" + wednesday + '\'' +
                ", thursday='" + thursday + '\'' +
                ", friday='" + friday + '\'' +
                ", saturday='" + saturday + '\'' +
                ", sunday='" + sunday + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }
}
