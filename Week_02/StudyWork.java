package com.br.workflow;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : zhang.cao
 * @date : 2021/1/22 19:06
 */
@Slf4j
public class StudyWork {
    public static void main(String[] args) {
        String result = HttpUtil.get("http://localhost:8088/api/hello");
        log.info("请求结果:{}",result);
    }
}
