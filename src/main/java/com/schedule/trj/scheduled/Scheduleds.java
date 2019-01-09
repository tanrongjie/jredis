package com.schedule.trj.scheduled;

import com.schedule.trj.utils.RedisUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @BelongsProject: schedule
 * @BelongsPackage: com.schedule.trj.scheduled
 * @Author: 谭荣杰
 * @CreateTime: 2018-11-29 12:56
 * @Description: 定时任务
 */
@Component
public class Scheduleds {

    private final RedisUtils redisUtils;

    private static boolean flag = false;

    public Scheduleds(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 每隔2秒执行一次
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void setData() {
        if (!flag) {
            for (int i = 1; i <= 50; i++) {
                redisUtils.lpush("data", i + "===============");
            }
        }
        flag = true;
    }

    /**
     * 每隔两分钟执行一次
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void getRpt() {
        Lock lock = new ReentrantLock();
        for(int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public synchronized void run()  {
                    lock.lock();
                    Long length = redisUtils.llen("data");
                    List<String> list = redisUtils.lrange("data", 0, 19);
                    for (int j = 0, size = list.size(); j < size; j++) {
                        File file = new File("D:\\log.txt");
                        try {
                            FileWriter fw = new FileWriter(file, true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            String content = "读取数据:" + list.get(j);
                            bw.write(new String(content.getBytes(StandardCharsets.UTF_8)) + "\r\n");// 往已有的文件上添加字符串
                            bw.close();
                            fw.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String result = redisUtils.ltrim("data", 20L, length);
                    System.out.println(result);
                    lock.unlock();
                }
            }).start();
        }
    }
}
