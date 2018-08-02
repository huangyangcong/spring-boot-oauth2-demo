package com.luangeng.oauthserver.service;

import lombok.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://blog.csdn.net/biboheart/article/details/80666700
@Service
public class DingService {

    private String ding_url = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?";
    private String redirect_url = "https://www.baidu.com/";
    private String appid = "dingoa1ft83dapn4jhlxnn";
    private String appSecret = "vXwQ-JarhtSQmILQdh_ONSf0egP1BWnvsv63Lo3ISijtv3AVUZO66jqZq3fk_n6U";

    private String tmp_token_url = "https://oapi.dingtalk.com/sns/gettoken?appid=" + appid + "&appsecret=" + appSecret;
    private String per_token_url = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token="; //post
    private String sns_token_url = "https://oapi.dingtalk.com/sns/get_sns_token?access_token=";      //post
    private String user_info_url = "https://oapi.dingtalk.com/sns/getuserinfo?sns_token=";

    private RestTemplate template = new RestTemplate();

    public TmpToken getTmpToken() {
        ResponseEntity<TmpToken> tmpToken = template.getForEntity(tmp_token_url, TmpToken.class);
        if (tmpToken.getStatusCode().equals(HttpStatus.OK)) {
            return tmpToken.getBody();
        }
        return null;
    }

    public PerToken getPertoken(String code) {
        TmpToken tmpToken = getTmpToken();
        String token = tmpToken.getAccess_token();
        String param = "{'tmp_auth_code':'" + code + "'}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        PerToken perToken = template.postForObject(per_token_url + token, requestEntity, PerToken.class);
        perToken.setTmpToken(token);
        return perToken;
    }

    public SnsToken getSnsToken(String code) {
        PerToken perToken = getPertoken(code);
        String param = "{'openid': '" + perToken.getOpenid() + "','persistent_code': '" + perToken.getPersistent_code() + "'}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        SnsToken s = template.postForObject(sns_token_url + perToken.getTmpToken(), requestEntity, SnsToken.class);
        return s;
    }

    public UserInfo getuserInfo(String code) {
        SnsToken snsToken = getSnsToken(code);
        ResponseEntity<ResuserInfo> tmpToken = template.getForEntity(user_info_url + snsToken.getSns_token(), ResuserInfo.class);
        if (tmpToken.getStatusCode().equals(HttpStatus.OK)) {
            return tmpToken.getBody().getUser_info();
        }
        return null;
    }

    @Data
    private static class DingBase {
        private long errcode;
        private String errmsg;
    }

    @Data
    private static class TmpToken extends DingBase {
        private String access_token;
    }

    @Data
    private static class PerToken {
        private String openid;
        private String unionid;
        private String persistent_code;
        private String tmpToken;
    }

    @Data
    private static class SnsToken {
        private String sns_token;
    }

    @Data
    public static class UserInfo {
        private String dingId;
        private String nick;
        private String unionid;
        private String openid;
    }

    @Data
    public static class ResuserInfo {
        private UserInfo user_info;
    }

}
