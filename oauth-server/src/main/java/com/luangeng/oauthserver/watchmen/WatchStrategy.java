package com.luangeng.oauthserver.watchmen;

public enum WatchStrategy {

    SESSION {
        @Override
        String getvalue(RequestDesc desc) {
            return desc.getSessionId();
        }
    },

    USER_CODE {
        @Override
        String getvalue(RequestDesc desc) {
            return desc.getUserCode();
        }
    },

    IP {
        @Override
        String getvalue(RequestDesc desc) {
            return desc.getIp();
        }
    },

    METHOD {
        @Override
        String getvalue(RequestDesc desc) {
            return desc.getMethod();
        }
    },

    FORM {
        @Override
        String getvalue(RequestDesc desc) {
            return SESSION.getvalue(desc);
        }

    };

    String getvalue(RequestDesc desc) {
        return "";
    }

}
