package me.xueyao.interceptor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.xueyao.annotation.SameUrlRequest;
import me.xueyao.filter.RequestWrapper;
import me.xueyao.util.IOUtils;
import me.xueyao.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义重复提交拦截器
 * @author Simon.Xue
 * @date 2020-06-19 10:03
 **/
@Slf4j
@Component
public class SameUrlRequestInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SameUrlRequest annotation = method.getAnnotation(SameUrlRequest.class);
            if (null != annotation) {
                if (requestDataValidator(request)) {
                    log.info("---- 接口重复提交 ----");
                    return false;
                }
                return true;
            }
            return true;
        }
        return super.preHandle(request, response, handler);
    }

    public boolean requestDataValidator(HttpServletRequest request) throws Exception {
        //防止request使用用一次后丢失流数据
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String body = IOUtils.read(requestWrapper.getReader());

        StringBuilder sb = new StringBuilder();
        sb.append(JSONObject.toJSONString(requestWrapper.getParameterMap()));
        sb.append(body);

        String url = requestWrapper.getRequestURI();
        Map<String, String> map = new HashMap<>(16);
        map.put(url, sb.toString());

        String nowUrlParams = map.toString();
        Object value = redisUtil.get(url);

        if (null == value) {
            redisUtil.set(url, nowUrlParams, 1);
            log.info("当前时间：{},设置params:{}",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), nowUrlParams);
            return false;
        }

        String preUrlParams = String.valueOf(redisUtil.get(url));
        if (preUrlParams.equals(nowUrlParams)) {
            log.info("当前时间：{},重复提交params:{}",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), preUrlParams);
            return true;
        }


        redisUtil.set(url, nowUrlParams, 1);
        log.info("当前时间：{},重新设置params:{}",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), nowUrlParams);
        return false;
    }


}
