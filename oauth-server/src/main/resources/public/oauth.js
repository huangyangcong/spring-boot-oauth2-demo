import 'url-search-params-polyfill';

import VueCookie from 'vue-cookie';
import axios from 'axios';
import rs from 'jsrsasign';
import config from './config';

const Index = 'auth';
const KEY = 'key';
const USER = 'user';

const FULL_CHARTER = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopgrstuvwxyz';

class OAuth {
  getAuthInfo() {
    let auth = sessionStorage.getItem(Index);
    return JSON.parse(auth);
  }

  setAuthInfo(authInfo) {
    sessionStorage.setItem(Index, JSON.stringify(authInfo));
  }

  clearAuthInfo() {
    sessionStorage.removeItem(Index);
    sessionStorage.removeItem(KEY);
    sessionStorage.removeItem(USER);
  }

  getPublicKey() {
    return sessionStorage.getItem(KEY);
  }

  setPublicKey(key) {
    return sessionStorage.setItem(KEY, key);
  }

  clearPublicKye(){
    return sessionStorage.removeItem(KEY);
  }

  getUserInfo() {
    let user = sessionStorage.getItem(USER);
    return JSON.parse(user);
  }

  setUserInfo(user) {
    sessionStorage.setItem(USER, JSON.stringify(user));
  }

  clearUserInfo(){
    return sessionStorage.removeItem(USER);
  }

  clearAll(){
    this.clearPublicKye();
    this.clearUserInfo();
    this.clearAuthInfo();
    VueCookie.delete("redirect_uri");
  }

  isAuthenticated() {
    // 从sessionStorage中读取登录信息。
    let auth = this.getAuthInfo();

    if (auth !== null && auth.access_token !== undefined) {
      // 验证token.
      let key = this.getPublicKey();
      let jwtToken = auth.access_token;
      let jwsToken = auth.access_token.slice(0, auth.access_token.lastIndexOf('.'));
      let jws = rs.jws.JWS;
      let header = jws.parse(jwsToken).headerObj;
      let verifyResult = jws.verifyJWT(jwtToken, key, {
        alg: [header.alg]
      });
      return verifyResult;
    }
    return false;
  }

  /**
   * 用户授权
   */
  authenticate(to) {
    // 获取授权代码
    let code = to.query['code'];
    if (code === undefined) {
      let redirectUri = window.location.origin + '/' + to.fullPath;
      this.redirectToLogin(redirectUri);
      return true;
    }

    let state = to.query['state'];
    return this.retrieveToken(code, state);
  }

  redirectToLogin(redirectUri) {
    // 清空缓存的token信息。
    this.clearAuthInfo();

    // 刷新state
    let c1 = FULL_CHARTER[Math.floor(Math.random() * 52)];
    let c2 = FULL_CHARTER[Math.floor(Math.random() * 52)];
    let c3 = FULL_CHARTER[Math.floor(Math.random() * 52)];
    let c4 = FULL_CHARTER[Math.floor(Math.random() * 52)];
    let c5 = FULL_CHARTER[Math.floor(Math.random() * 52)];
    let c6 = FULL_CHARTER[Math.floor(Math.random() * 52)];

    // 保存redirect_uri到cookie中.
    // let redirectUri = window.location.origin + '/' + currentPath;
    VueCookie.set('redirect_uri', redirectUri);
    // this.$cookie.set('redirect_uri', redirectUri);

    // 没有授权代码，重定向到授权页面
    let path = config.oauth2.ssoServer;
    path = path + '/oauth/authorize?client_id=';
    path = path + config.oauth2.clientId;
    path = path + '&client_secret=';
    path = path + config.oauth2.clientSecret;
    path = path + '&response_type=code&state=';
    path = path + (c1 + c2 + c3 + c4 + c5 + c6);
    path = path + '&redirect_uri=';
    path = path + encodeURIComponent(redirectUri);
    window.location = path;
  }

  refreshToken() {
    let self = this;
    // form数据
    let form = new URLSearchParams();
    form.append('grant_type', 'refresh_token');
    form.append('refresh_token', this.getAuthInfo().refresh_token);

    var http = axios.create({
      baseURL: '/oauth',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    return http.post('/refresh_token', form).then(function (response) {
      if (response.status === 200) {
        // 返回成功
        self.setAuthInfo(response.data);
        return true;
      }
      return false;
    }).catch(function (error) {
      console.error(error);
      return false;
    });
  }

  logout(){
    this.clearAuthInfo();
    window.location = "http://localhost:8080/logout?redirect_uri=http://localhost:8081";
  }

  retrieveToken(code, state) {
    let self = this;
    let redirectUri = VueCookie.get('redirect_uri');
    // form数据
    let form = new URLSearchParams();
    form.append('state', state);
    form.append('grant_type', 'authorization_code');
    form.append('code', code);
    form.append('redirect_uri', redirectUri);
    let http = axios.create({
      baseURL: '/oauth',
      auth: {
        username: config.oauth2.clientId,
        password: config.oauth2.clientSecret
      },
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    return http.post('/token', form).then(function (response) {
      if (response.status === 200) {
        // 返回成功
        self.setAuthInfo(response.data);
        // 设置登录信息.
        let accessToken = response.data.access_token;
        let jwsToken = accessToken.slice(0, accessToken.lastIndexOf('.'));
        let jws = rs.jws.JWS;
        let payload = jws.parse(jwsToken).payloadObj;
        self.setUserInfo({
          user: payload.user_name,
          roles: payload.authorities
        });

        // 读取可以
        let key = sessionStorage.getItem(KEY);
        if (key == null) {
          // 获取key
          let http = axios.create({
            baseURL: '/oauth'
          });
          let authInfo = self.getAuthInfo();
          http.defaults.headers.Authorization = authInfo.token_type + ' ' + authInfo.access_token;
          return http.get('token_key').then(response => {
            self.setPublicKey(response.data.value);
            return true;
          });
        }
        return true;
      }
      return new Error('oops');
    }).catch(function (error) {
      console.error(error);
      return error;
    });
  }
}

export default new OAuth();
