package me.xueyao.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.xueyao.annotation.SameUrlRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 重复提交
 * @author Simon.Xue
 * @date 2020-06-19 11:00
 **/

@RestController
@RequestMapping("/sameUrl")
@Slf4j
public class SameUrlController {

    /**
     * post提交数据
     */
    @PostMapping("/postRequest")
    @SameUrlRequest
    public void postRequest(@RequestBody JSONObject json) {

        log.info("当前提交的值：{}", json.getString("name"));
    }

    /**
     * get提交数据
     */
    @GetMapping("/getRequest")
    @SameUrlRequest
    public void getRequest(@RequestParam("name") String name) {
        log.info("当前提交的值：{}", name);
    }

    /**
     * get提交数据
     */
    @GetMapping("/getRequest/{name}")
    @SameUrlRequest
    public void getPathRequest(@PathVariable("name") String name) {
        log.info("当前提交的值：{}", name);
    }
}
