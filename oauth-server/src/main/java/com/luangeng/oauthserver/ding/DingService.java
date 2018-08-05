package com.luangeng.oauthserver.ding;

import lombok.Data;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 钉钉扫码登录验证服务
 */
@Service
public class DingService {

    private static final int TIMEOUT = 4000;

    private String appid = "dingoajyvqz1nazpqywfxn";
    private String appSecret = "qkaPvilixjaoeKI8pFS9aaAyp7y00Zt6q2xCcg4O3JsiKcoYHxQNLoN5eWXoBfpS";

    private String tmp_token_url = "https://oapi.dingtalk.com/sns/gettoken?appid=" + appid + "&appsecret=" + appSecret;
    private String per_token_url = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token=";
    private String sns_token_url = "https://oapi.dingtalk.com/sns/get_sns_token?access_token=";
    private String user_info_url = "https://oapi.dingtalk.com/sns/getuserinfo?sns_token=";

    private RestTemplate template;

    public DingService() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(TIMEOUT);
        requestFactory.setReadTimeout(TIMEOUT);
        template = new RestTemplate(requestFactory);
    }

    public TmpToken getTmpToken() {
        ResponseEntity<TmpToken> tmpToken = template.getForEntity(tmp_token_url, TmpToken.class);
        if (tmpToken.getStatusCode().equals(HttpStatus.OK)) {
            return tmpToken.getBody();
        }
        return null;
    }

    public PerToken getPertoken(String code) {
        TmpToken tmpToken = getTmpToken();
        checkSuccess(tmpToken);
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
        checkSuccess(perToken);
        String param = "{'openid': '" + perToken.getOpenid() + "','persistent_code': '" + perToken.getPersistent_code() + "'}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        SnsToken s = template.postForObject(sns_token_url + perToken.getTmpToken(), requestEntity, SnsToken.class);
        return s;
    }

    public UserInfo getUserInfo(String code) {
        SnsToken snsToken = getSnsToken(code);
        checkSuccess(snsToken);
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

    private boolean checkSuccess(DingBase data) {
        if (data != null && data.getErrcode() == 0) {
            return true;
        }
        return false;
    }

    @Data
    private static class PerToken extends DingBase {
        private String openid;
        private String unionid;
        private String persistent_code;
        private String tmpToken;
    }

    @Data
    public static class UserInfo {
        private String dingId;
        private String nick;
        private String unionid;
        private String openid;
    }

    @Data
    private static class SnsToken extends DingBase {
        private String sns_token;
    }

    @Data
    public static class ResuserInfo extends DingBase {
        private UserInfo user_info;
    }

}
