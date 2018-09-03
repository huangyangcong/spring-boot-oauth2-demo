package com.luangeng.oauthserver.watchmen.strategy;

import com.luangeng.oauthserver.watchmen.RequestDesc;
import com.luangeng.oauthserver.watchmen.WatchException;

/**
 * 防止表单重复提交
 */
public class SessionStrage implements CountStrage {
    @Override
    public String put(RequestDesc desc) {
        return desc.getSessionId();
    }

    public void out() throws WatchException {
        throw new WatchException("操作太频繁.");
    }
}
