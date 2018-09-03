package com.luangeng.oauthserver.watchmen;

/**
 * 请求描述上下文容器
 */
public class RequestDescContextHolder {

    private static final ThreadLocal<RequestDesc> requestDescHolder = new ThreadLocal<>();

    public static void reset() {
        requestDescHolder.remove();
    }

    public static void set(RequestDesc desc) {
        requestDescHolder.set(desc);
    }

    public static RequestDesc get() {
        RequestDesc desc = requestDescHolder.get();
        return desc;
    }


}
