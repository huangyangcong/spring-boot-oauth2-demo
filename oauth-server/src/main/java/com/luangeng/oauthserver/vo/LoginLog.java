package com.luangeng.oauthserver.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginLog {

    private Long id;

    private String userCode;//登录账号

    private String opration; //登录 登出

    private String type;    //扫码，密码

    private String status;  //成功 失败

    private String failMsg; //登录失败原因

    private Date time;     //操作时间

    private String origin;  //登录来源

    private String ip;     //ip

    private String ipLocation;  //ip所在地

    private String os;     //user-agent

    private String application; //client
}
