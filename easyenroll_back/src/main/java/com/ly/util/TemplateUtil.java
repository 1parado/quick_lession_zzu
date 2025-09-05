package com.ly.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplateUtil {
    public static String renderTemplate(String template, String name, Date date) {
        if (template == null) {
            return "";  // 如果模板为空，返回空字符串
        }

        // 替换姓名占位符
        String result = template.replace("{{name}}", name != null ? name : "");

        // 替换时间占位符
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = dateFormat.format(date);
            result = result.replace("{{time}}", dateString);
        } else {
            result = result.replace("{{time}}", "");  // 如果日期为空，替换为空字符串
        }

        return result;
    }
}
