const dingUrl = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?";
const redirect_uri = "https://www.baidu.com/";
const appid = "dingoa1ft83dapn4jhlxnn";
var state = "";


function opending(){
        var obj = DDLogin({
     id:"login_container",//这里需要你在自己的页面定义一个HTML标签并设置id，例如<div id="login_container"></div>或<span id="login_container"></span>
     goto: encodeURIComponent(dingUrl+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+rediretct),
     width : "300",
     height: "300"
 });
}


var hanndleMessage = function (event) {
    var origin = event.origin;
    console.log("origin", event.origin);
    //判断是否来自ddLogin扫码事件。
    if( origin == "https://login.dingtalk.com" ) {
        var loginTmpCode = event.data;
        //拿到loginTmpCode后,在这里构造跳转链接进行跳转
        console.log("loginTmpCode", loginTmpCode);
        window.parent.postMessage(loginTmpCode,'*');
        window.location.href=dingUrl+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+rediretct+"&loginTmpCode="+loginTmpCode;
    }
};

if (typeof window.addEventListener != 'undefined') {
    window.addEventListener('message', hanndleMessage, false);
} else if (typeof window.attachEvent != 'undefined') {
    window.attachEvent('onmessage', hanndleMessage);
}


