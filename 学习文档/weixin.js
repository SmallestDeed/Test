
$(function() {
wx.ready(function(){
        //分享朋友
        wx.onMenuShareAppMessage({
			
            title: '【好友力荐】现摘现卖，绿色无污染，爱嫒38号，快来老妈商城', // 分享标题
            desc: '立刻领取优惠券，享更低价', // 分享描述
            link: 'http://open.weixin.qq.com/connect/oauth2/authorize?appid='wxa4469baa8546fa4a'&redirect_uri=www.baidu.com', // 分享链接
            imgUrl: 'http://www.****.com/*****/static/img/line.png', // 分享图标
            trigger: function (res) {
                    alert(res.);
                },
            success: function () { 
                // 用户确认分享后执行的回调函数
                alert("分享成功");
                // 用户确认分享后执行的回调函数,跳转后台
                //获取openid
                var openid = $("#openid").val();
                location.href = "/*****/shareOk?openid="+openid;
            },
            cancel: function () { 
                // 用户取消分享后执行的回调函数
                alert("分享失败");
            }
        });
        //分享朋友圈
        wx.onMenuShareTimeline({
            title: '【好友力荐】现摘现卖，绿色无污染，爱嫒38号，快来老妈商城', // 分享标题
            link: 'http://open.weixin.qq.com/connect/oauth2/authorize?appid='wxa4469baa8546fa4a'&redirect_uri=www.baidu.com', // 分享链接
            imgUrl: 'http://www.*****.com/******/static/img/line.png', // 分享图标
		
            success: function () { 
                alert("分享成功");
                // 用户确认分享后执行的回调函数,跳转后台
                //获取openid
                var openid = $("#openid").val();
                location.href = "/*******/shareOk?openid="+openid;  
            },
            cancel: function () { 
                // 用户取消分享后执行的回调函数
                alert("分享失败");
            }
        });

    });
	});