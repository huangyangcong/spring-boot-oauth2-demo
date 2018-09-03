package com.luangeng.oauthserver.support;

import com.luangeng.oauthserver.service.LoginLogService;
import com.luangeng.oauthserver.vo.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 认证监听器
 */
@Component
public class AuthenticationEventListener {

    @Autowired
    private LoginLogService loginLogService;

    @EventListener
    public void handleAuthenticateSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        saveLog(event, "LoginRecord.STATUS_SUCCESS");
    }

    @EventListener
    public void handleAuthenticateBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        saveLog(event, "LoginRecord.STATUS_FAIL_BAD_CREDENTIALS");
    }

    @EventListener
    public void handleAuthenticateDisabled(AuthenticationFailureDisabledEvent event) {
        saveLog(event, "LoginRecord.STATUS_FAIL_DISABLED");
    }

    @EventListener
    public void handleAuthenticateCredentialsExpired(AuthenticationFailureCredentialsExpiredEvent event) {
        saveLog(event, "LoginRecord.STATUS_FAIL_CREDENTIALS_EXPIRED");
    }

    private void saveLog(AbstractAuthenticationEvent event, String statusSuccess) {
        Authentication authentication = event.getAuthentication();
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webDetail = (WebAuthenticationDetails) details;
            LoginLog log = new LoginLog();
            log.setIp(webDetail.getRemoteAddress());
            loginLogService.saveLog(log);
        }
    }

}
