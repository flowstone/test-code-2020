package me.xueyao.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 替换request
 * @author Simon.Xue
 * @date 2020-06-19 14:29
 **/
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "replaceStreamFilter")
public class ReplaceStreamFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("---- start replaceStreamFilter ----");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //RequestWrapper替换原来的request
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        log.info("---- stop replaceStreamFilter ----");

    }
}
