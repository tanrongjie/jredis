package com.schedule.trj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @BelongsProject: sms
 * @BelongsPackage: com.bxzt.sms.utils
 * @Author: 谭荣杰
 * @CreateTime: 2018-10-25 15:05
 * @Description: url工具类
 */
public class URLUtils {

    /**
     * 测试请求url地址是否返回200状态码
     *
     * @param webUrl 请求地址
     * @return Boolean
     */
    public static String testConnectionUrl(String webUrl) {
        try {
            // 设置此类是否应该自动执行 HTTP重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
            // 到URL所引用的远程对象的连接
            HttpURLConnection conn = (HttpURLConnection) new URL(webUrl).openConnection();
            // 设置超时时间
            conn.setConnectTimeout(3000);
            // 设置URL请求的方法，GET POST HEAD OPTIONS PUT DELETE TRACE
            // 以上方法之一是合法的，具体取决于协议的限制。
            conn.setRequestMethod("GET");
            System.out.println(conn.getResponseCode());
            return readResponseContent(conn.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    private static String readResponseContent(InputStream in) throws IOException {
        Reader reader;
        StringBuilder content = new StringBuilder();
        reader = new InputStreamReader(in);
        char[] buffer = new char[1024];
        int head;
        while ((head = reader.read(buffer)) > 0) {
            content.append(new String(buffer, 0, head));
        }
        in.close();
        reader.close();
        return content.toString();
    }
}
