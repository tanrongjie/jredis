package com.schedule.trj;

import com.schedule.trj.utils.RedisUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("")
public class TestController {

    private final RedisUtils redisUtils;

    private static int i = 0;

    public TestController(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void index(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        for(Map.Entry<String, String[]> data : map.entrySet()) {
            System.out.print(data.getKey() + ":");
            for(String value : data.getValue()) {
                redisUtils.set(data.getKey() + "_" + i, value);
            }
            System.out.println();
        }
    }
}
