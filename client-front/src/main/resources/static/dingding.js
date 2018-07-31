///API   https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.wcYmTe&treeId=168&articleId=104882&docType=1

const ding_url = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?";
const redirect_uri = "https://www.baidu.com/";
const appid = "dingoa1ft83dapn4jhlxnn";
const appSecret = "vXwQ-JarhtSQmILQdh_ONSf0egP1BWnvsv63Lo3ISijtv3AVUZO66jqZq3fk_n6U";

const tmp_token_url = "https://oapi.dingtalk.com/sns/gettoken?appid="+appid+"&appsecret="+appSecret;
const per_token_url = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token=" //post
const sns_token_url = "https://oapi.dingtalk.com/sns/get_sns_token?access_token=";      //post
const user_info_url = "https://oapi.dingtalk.com/sns/getuserinfo?sns_token=";

var state = "";
var tmp_auth_code;
var tmp_token;
var per_token;
var sns_token;


function opending(){
        var obj = DDLogin({
     id:"login_container",//这里需要你在自己的页面定义一个HTML标签并设置id，例如<div id="login_container"></div>或<span id="login_container"></span>
     goto: encodeURIComponent(ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+rediretct),
     width : "300",
     height: "300"
 });
}


var hanndleMessage = function (event) {
    var origin = event.origin;
    console.log("origin", event.origin);
    //判断是否来自ddLogin扫码事件。
    if( origin == "https://login.dingtalk.com" ) {
        tmp_auth_code = event.data;
        //拿到loginTmpCode后,在这里构造跳转链接进行跳转
        console.log("loginTmpCode", tmp_auth_code);
        window.parent.postMessage(tmp_auth_code,'*');
        window.location.href=ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+rediretct+"&loginTmpCode="+tmp_auth_code;
    }
};

if (typeof window.addEventListener != 'undefined') {
    window.addEventListener('message', hanndleMessage, false);
} else if (typeof window.attachEvent != 'undefined') {
    window.attachEvent('onmessage', hanndleMessage);
}

function getTmpToken(){

}


