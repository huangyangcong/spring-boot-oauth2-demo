const FULL_CHARTER = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopgrstuvwxyz';
const ding_url = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?";
const redirect_url = "http://localhost:9000/server/auth";
const appid = "dingoajyvqz1nazpqywfxn";
const state="qwer";

function makeState(){
var state='';
for (var a=0;a<4;a++){
    state+=FULL_CHARTER[Math.floor(Math.random() * 52)];
}
return state;
}


function openDing(){
  var obj = DDLogin({
     id:"login_container",//这里需要你在自己的页面定义一个HTML标签并设置id，例如<div id="login_container"></div>或<span id="login_container"></span>
     goto: encodeURIComponent(ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+redirect_url),
     width : "300",
     height: "300"
 });
}

function openWeixin(){
    var obj = new WxLogin({
 self_redirect:true,
 id:"login_container",
 appid: "wxbdc5610cc59c1631",
 scope: "snsapi_login",
 redirect_uri: encodeURIComponent(redirect_url),
  state: makeState(),
 style: "black"
 });
}

var hanndleMessage = function (event) {
    var origin = event.origin;
    console.log("origin", event.origin);
    //判断是否来自ddLogin扫码事件。
    if( origin == "https://login.dingtalk.com" ) {
        var tmp_code = event.data;
        //拿到loginTmpCode后,在这里构造跳转链接进行跳转
        window.parent.postMessage(tmp_code,'*');
        window.location.href=ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+redirect_url+"&loginTmpCode="+tmp_code;
    }
};

if (typeof window.addEventListener != 'undefined') {
    window.addEventListener('message', hanndleMessage, false);
} else if (typeof window.attachEvent != 'undefined') {
    window.attachEvent('onmessage', hanndleMessage);
}