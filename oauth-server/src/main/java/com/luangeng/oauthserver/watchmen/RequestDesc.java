package com.luangeng.oauthserver.watchmen;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class RequestDesc {

    private String userCode;//登录账号

    private long time;     //操作时间

    private String sessionId;


    //////////////////////////////Local

    private String protcool;

    private String url;

    private String method;


    ///////////////////////////Remote

    private String ip;     //ip

    private String ipLocation;  //ip所在地

    private String agent;     //user-agent

    private String origin; //client


    public static RequestDesc newFrom(HttpServletRequest request) {
        RequestDesc desc = new RequestDesc();
        desc.setSessionId(request.getSession().getId());
        desc.setIp(request.getRemoteAddr());
//        desc.setIpLocation(request.getRemoteHost());
        desc.setMethod(request.getMethod());
        desc.setOrigin(request.getHeader("Origin"));
        desc.setAgent(request.getHeader("User-Agent"));
        desc.setProtcool(request.getProtocol());
        desc.setTime(System.currentTimeMillis());
        desc.setUrl(request.getRequestURI());
        desc.setUserCode("");
        return desc;
    }

}
