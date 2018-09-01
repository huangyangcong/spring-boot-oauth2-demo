package com.luangeng.oauthserver.service;

import com.luangeng.oauthserver.exception.DingServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 钉钉扫码登录验证服务
 */
@Slf4j
@Service
public class DingdingService {

    private static final int DING_OK = 0;
    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_DELAY = 1000 * 60 * 3;
    private static final int REQUEST_TIMEOUT = 1000 * 40;
    private static final int TOKEN_REFRESH_FIXED_RATE = 1000 * 60 * 110;

    private static final String ACC_TOKEN_URL = "https://oapi.dingtalk.com/sns/gettoken?appid=%s&appsecret=%s";
    private static final String PER_TOKEN_URL = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token=";
    private static final String USER_ID_URL = "https://oapi.dingtalk.com/user/getUseridByUnionid?access_token=%s&unionid=%s";
    private static final String CORP_TOKEN_URL = "https://oapi.dingtalk.com/gettoken?corpid=%s&corpsecret=%s";
    private static final String USER_DETAIL_URL = "https://oapi.dingtalk.com/user/get?access_token=%s&userid=%s";

    @Value("${dingding.appId}")
    private String appId;

    @Value("${dingding.appSecret}")
    private String appSecret;

    @Value("${dingding.corpId}")
    private String corpId;

    @Value("${dingding.corpSecret}")
    private String corpSecret;

    private int retryCount = 0;

    private RestTemplate template;

    //有效期为7200秒
    private AccessToken accessToken;

    //有效期为7200秒
    private CorpToken corpToken;

    public DingdingService() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(REQUEST_TIMEOUT);
        requestFactory.setReadTimeout(REQUEST_TIMEOUT);
        template = new RestTemplate(requestFactory);
    }

    private void getAccessToken() throws InterruptedException {
        String url = String.format(ACC_TOKEN_URL, appId, appSecret);
        ResponseEntity<AccessToken> accessTokenVal = template.getForEntity(url, AccessToken.class);
        if (accessTokenVal.getStatusCode().equals(HttpStatus.OK)) {
            retryCount = 0;
            AccessToken token = accessTokenVal.getBody();
            if (token != null && token.getErrcode() == DING_OK) {
                accessToken = token;
            } else {
                log.error("获取钉钉token失败：" + (token == null ? "获取AccessToken为空" : token.getErrmsg()));
            }
        } else if (retryCount <= MAX_RETRY_COUNT) {
            retryCount++;
            log.error("获取钉钉token失败：获取AccessToken失败");
            Thread.sleep(RETRY_DELAY);
            getAccessToken();
            retryCount = 0;
        }
    }

    private void getCorpToken() throws InterruptedException {
        String url = String.format(CORP_TOKEN_URL, corpId, corpSecret);
        ResponseEntity<CorpToken> accessTokenVal = template.getForEntity(url, CorpToken.class);
        if (accessTokenVal.getStatusCode().equals(HttpStatus.OK)) {
            retryCount = 0;
            CorpToken token = accessTokenVal.getBody();
            if (token != null && token.getErrcode() == DING_OK) {
                corpToken = token;
            } else {
                log.error("获取钉钉token失败：" + (token == null ? "获取CorpToken为空" : token.getErrmsg()));
            }
        } else if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.error("获取钉钉token失败：获取CorpToken失败");
            Thread.sleep(RETRY_DELAY);
            getCorpToken();
            retryCount = 0;
        }
    }

    /**
     * 获取永久token
     */
    private PerToken getPerToken(String code) {
        checkSuccess(accessToken);
        String token = accessToken.getAccess_token();
        String param = "{'tmp_auth_code':'" + code + "'}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
        PerToken perToken = template.postForObject(PER_TOKEN_URL + token, requestEntity, PerToken.class);
        perToken.setTmpToken(token);
        return perToken;
    }

    /**
     * 获取工号
     */
    public String getEmployeeNum(String code) {
        PerToken info = getPerToken(code);
        checkSuccess(info);
        ResponseEntity<UserId> user = template.getForEntity(String.format(USER_ID_URL, corpToken.getAccess_token(),
                info.getUnionid()), UserId.class);
        if (user.getStatusCode().equals(HttpStatus.OK)) {
            UserId id = user.getBody();
            if (id != null) {
                return id.getUserid();
            }
        }
        return null;
    }

    /**
     * 获取详细信息
     */
    public UserDetail getUserDetail(String code) {
        String userId = getEmployeeNum(code);
        ResponseEntity<UserDetail> user = template.getForEntity(String.format(USER_DETAIL_URL, corpToken.getAccess_token(),
                userId), UserDetail.class);
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
        private String userid;
        private String position;
        private String unionid;
    }

    @Component
    private class TimedTask {
        @Scheduled(fixedRate = TOKEN_REFRESH_FIXED_RATE)
        public void fetchToken() throws InterruptedException {
            getAccessToken();
            getCorpToken();
        }
    }

}
