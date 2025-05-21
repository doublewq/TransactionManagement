package com.hometask.transactionmanagement.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@Slf4j
public class RequestContent {
    public static ServletRequestAttributes getRequestAttribute() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttribute().getRequest();
    }

    public static void inherit() {
        try {
            RequestContextHolder.setRequestAttributes(getRequestAttribute(), true);
        } catch (Exception ignore) {
            // 记录日志
            log.error("ignore");
        }
    }

    public static String getUserId() {
        return Stream.of(getRequest().getHeader("userId"), getRequest().getHeader("User"))
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(StringUtils.EMPTY);
    }
}
