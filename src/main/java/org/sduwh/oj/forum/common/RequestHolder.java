package org.sduwh.oj.forum.common;

import org.sduwh.oj.forum.param.UserParam;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {

    private static final ThreadLocal<UserParam> userHolder = new ThreadLocal<UserParam>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(UserParam userParam) {
        userHolder.set(userParam);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static UserParam getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
