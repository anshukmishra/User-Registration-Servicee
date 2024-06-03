package com.example.a.filter;

import com.example.a.entity.ApiLog;
import com.example.a.service.ApiLogProducerService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.Enumeration;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE) //TODO: explain what is this:- I guess it gives guarantee that this filter will be executed first as I'm printing apiLog for that we need this filter to be called before any other filter. Although here it is not necessary as we are using only one filter.
public class CustomFilter implements Filter {
    @Autowired
    ApiLogProducerService apiLogProducerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.info("########## Initiating Custom filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        ApiLog apiLog = new ApiLog();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        apiLog.setRequestTimeStamp(now);
        apiLog.setMethod(request.getMethod());
        apiLog.setReqPath(request.getRequestURI());
        StringBuilder requestParam= new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);

            requestParam.append(paramName);
            requestParam.append(": ");
            requestParam.append(paramValue);
            requestParam.append("\n");
        }
        apiLog.setRequestBody(requestParam);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);
        apiLog.setResStatus(response.getStatus());
        String capturedResponseBody = responseWrapper.getCapturedResponseBody();
        apiLog.setResponseBody(capturedResponseBody);
//        LOGGER.info(capturedResponseBody);
        response.getWriter().write(capturedResponseBody);
        apiLogProducerService.sendLog(apiLog);
        LOGGER.info("Logging Response :{}", response.getContentType());
    }

    @Override
    public void destroy() {

    }

}
