package com.luangeng.oauthserver.ding;

import com.luangeng.oauthserver.exception.DingServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 钉钉扫码登录验证服务
 */
@Slf4j
@Service
@EnableScheduling
public class DingService {

    private static final int DING_OK = 0;
    private static final int RETRY_COUNT = 2;
    private static final int RETRY_DELAY = 1000 * 60 * 4;
    private static final int REQUEST_TIMEOUT = 4000;
    private static final int TOKEN_REFRESH_FIXED_RATE = 1000 * 60 * 110;
    private static final String JSON_TYPE = "application/json; charset=UTF-8";

    private static final String acc_token_url = "https://oapi.dingtalk.com/sns/gettoken?appid=%s&appsecret=%s";
    private static final String per_token_url = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token=";
    private static final String sns_token_url = "https://oapi.dingtalk.com/sns/get_sns_token?access_token=";
    private static final String user_info_url = "https://oapi.dingtalk.com/sns/getuserinfo?sns_token=";
    private static final String user_id_url = "https://oapi.dingtalk.com/user/getUseridByUnionid?access_token=%s&unionid=%s";
    private static final String corp_token_url = "https://oapi.dingtalk.com/gettoken?corpid=%s&corpsecret=%s";
    private static final String user_detail_url = "https://oapi.dingtalk.com/user/get?access_token=%s&userid=%s";

    private int failCount = 0;

    @Value("${dingding.appId}")
    private String appId;

    @Value("${dingding.appSecret}")
    private String appSecret;

    @Value("${dingding.corpId}")
    private String corpId;

    @Value("${dingding.corpSecret}")
    private String corpSecret;

    private RestTemplate template;

    private AccessToken accessToken = null;

    private CorpToken corpToken = null;

    public DingService() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(REQUEST_TIMEOUT);
        requestFactory.setReadTimeout(REQUEST_TIMEOUT);
        template = new RestTemplate(requestFactory);
    }

    public void getAccessToken() throws InterruptedException {
        String url = String.format(acc_token_url, appId, appSecret);
        ResponseEntity<AccessToken> accessTokenVal = template.getForEntity(url, AccessToken.class);
        if (accessTokenVal.getStatusCode().equals(HttpStatus.OK)) {
            failCount = 0;
            AccessToken token = accessTokenVal.getBody();
            if (token != null && token.getErrcode() == DING_OK) {
                accessToken = token;
            } else {
                log.error("获取钉钉token失败：" + (token == null ? "返回值为空" : token.getErrmsg()));
            }
        } else if (failCount < RETRY_COUNT) {
            failCount++;
            log.error("获取钉钉token失败：请求未返回成功");
            Thread.sleep(RETRY_DELAY);
            getAccessToken();
        }
    }

    public void getCorpToken() {
        String url = String.format(corp_token_url, corpId, corpSecret);
        ResponseEntity<CorpToken> accessTokenVal = template.getForEntity(url, CorpToken.class);
        corpToken = accessTokenVal.getBody();
    }

    public PerToken getPerToken(String code) {
        checkSuccess(accessToken);
        String token = accessToken.getAccess_token();
        String param = "{'tmp_auth_code':'" + code + "'}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(JSON_TYPE);
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        PerToken perToken = template.postForObject(per_token_url + token, requestEntity, PerToken.class);
        perToken.setTmpToken(token);
        return perToken;
    }

    public SnsToken getSnsToken(String code) {
        PerToken perToken = getPerToken(code);
        checkSuccess(perToken);
        String param = "{'openid': '" + perToken.getOpenid() + "','persistent_code': '" + perToken.getPersistent_code() + "'}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(JSON_TYPE);
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        SnsToken s = template.postForObject(sns_token_url + perToken.getTmpToken(), requestEntity, SnsToken.class);
        return s;
    }

    public ResUserInfo getUserInfo(String code) {
        SnsToken snsToken = getSnsToken(code);
        checkSuccess(snsToken);
        ResponseEntity<ResUserInfo> userInfo = template.getForEntity(user_info_url + snsToken.getSns_token(), ResUserInfo.class);
        if (userInfo.getStatusCode().equals(HttpStatus.OK)) {
            return userInfo.getBody();
        }
        return null;
    }

    public UserId getUserId(String code) {
        ResUserInfo info = getUserInfo(code);
        checkSuccess(info);
        ResponseEntity<UserId> user = template.getForEntity(String.format(user_id_url, corpToken.getAccess_token(),
                info.getUser_info().getUnionid()), UserId.class);
        if (user.getStatusCode().equals(HttpStatus.OK)) {
            return user.getBody();
        }
        return null;
    }

    public UserDetail getUserDetail(String code) {
        UserId info = getUserId(code);
        checkSuccess(info);
        ResponseEntity<UserDetail> user = template.getForEntity(String.format(user_detail_url, corpToken.getAccess_token(),
                info.getUserid()), UserDetail.class);
        if (user.getStatusCode().equals(HttpStatus.OK)) {
            return user.getBody();
        }
        return null;
    }

    private void checkSuccess(DingBase data) throws DingServiceException {
        if (data == null) {
            log.error("调用钉钉接口失败：请求未返回成功");
            throw new DingServiceException("调用钉钉接口失败：请求未返回成功");
        }
        if (data.getErrcode() != DING_OK) {
            log.error("调用钉钉接口失败：" + data.getErrmsg());
            throw new DingServiceException("调用钉钉接口失败：" + data.getErrmsg());
        }
    }

    @Data
    private static class DingBase {
        private int errcode;
        private String errmsg;
    }

    @Data
    private static class PerToken extends DingBase {
        private String openid;
        private String unionid;
        private String persistent_code;
        private String tmpToken;
    }

    @Data
    private static class AccessToken extends DingBase {
        private String access_token;
    }

    @Data
    public static class UserInfo {
        private String dingId;
        private String nick;
        private String unionid;
        private String openid;
    }

    @Data
    private static class ResUserInfo extends DingBase {
        private UserInfo user_info;
    }

    @Data
    private static class SnsToken extends DingBase {
        private String sns_token;
    }

    @Data
    private static class CorpToken extends DingBase {
        private String access_token;
        private String expires_in;
    }

    @Data
    private static class UserId extends DingBase {
        private String userid;
        private String contactType;
    }

    @Data
    public static class UserDetail extends DingBase {
        private String name;
        private String jobnumber;
        private String email;
        private String active;
        private String mobile;
    }

    private static class RetryClass {

    }

    @Component
    private class TimedTask {
        @Scheduled(fixedRate = TOKEN_REFRESH_FIXED_RATE)
        public void fetchAccessToken() throws InterruptedException {
            getAccessToken();
            getCorpToken();
        }
    }
}
