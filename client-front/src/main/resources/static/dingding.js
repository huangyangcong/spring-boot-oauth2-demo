const ding_url = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize?";
const redirect_url = "https://www.baidu.com/";
const appid = "dingoa1ft83dapn4jhlxnn";
const appSecret = "vXwQ-JarhtSQmILQdh_ONSf0egP1BWnvsv63Lo3ISijtv3AVUZO66jqZq3fk_n6U";

const tmp_token_url = "https://oapi.dingtalk.com/sns/gettoken?appid="+appid+"&appsecret="+appSecret;
const per_token_url = "https://oapi.dingtalk.com/sns/get_persistent_code?access_token="; //post
const sns_token_url = "https://oapi.dingtalk.com/sns/get_sns_token?access_token=";      //post
const user_info_url = "https://oapi.dingtalk.com/sns/getuserinfo?sns_token=";

var state = "qwer";
var tmp_auth_code;
var tmp_token;
var open_id;
var unionid;
var per_token;
var sns_token;


function opending(){
  var obj = DDLogin({
     id:"login_container",//这里需要你在自己的页面定义一个HTML标签并设置id，例如<div id="login_container"></div>或<span id="login_container"></span>
     goto: encodeURIComponent(ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+redirect_url),
     width : "300",
     height: "300"
 });
}

var hanndleMessage = function (event) {
    var origin = event.origin;
    console.log("origin", event.origin);
    //判断是否来自ddLogin扫码事件。
    if( origin == "https://login.dingtalk.com" ) {
        var tmp_code = event.data;
        //拿到loginTmpCode后,在这里构造跳转链接进行跳转
        alert(tmp_code);
        window.parent.postMessage(tmp_code,'*');
        window.location.href=ding_url+"appid="+appid+"&response_type=code&scope=snsapi_login&state="+state+"&redirect_uri="+redirect_url+"&loginTmpCode="+tmp_code;
    }
};

if (typeof window.addEventListener != 'undefined') {
    window.addEventListener('message', hanndleMessage, false);
} else if (typeof window.attachEvent != 'undefined') {
    window.attachEvent('onmessage', hanndleMessage);
}

function getTmpCode(){
    let params = window.location.search.split("&");
    tmp_auth_code = params[0].substr(params[0].indexOf("code=")+5);
    getTmpToken();
}

function getTmpToken(){
  $.ajax({
            url: tmp_token_url,
            type:"get",
            data: {"tmp_auth_code": tmp_auth_code},
            beforeSend(xhr){
                xhr.setRequestHeader("Access-Control-Allow-Origin", window.location.origin);
            },
            success: function (data) {
                tmp_token = data.access_token;
                getPerToken();
            },
            error:function(a,b,c){
                console.log(a.responseJSON);
            }
         });
}

function getPerToken(){
    $.ajax({
          url: per_token_url+tmp_token,
          type:"post",
          data: {"tmp_auth_code": tmp_auth_code},
          contentType: 'application/json',
          success: function (data) {
            per_token = data.persistent_code;
            open_id = data.openid;
            unionid = data.unionid;
            getSnsToken();
          },
          error:function(a,b,c){
           console.log(a.responseJSON);
          }
       });
}

function getSnsToken(){
    $.ajax({
         url: sns_token_url+per_token,
         type:"post",
         data: {"openid": open_id,
                "persistent_code": per_token},
         contentType: 'application/json',
         success: function (data) {
            sns_token = data.sns_token;
            getUserInfo();
         },
         error:function(a,b,c) {
          console.log(a.responseJSON);
         }
      });
}

function getUserInfo(){
     $.get(user_info_url+sns_token, function(data) {
          console.log(data);
     });
}


