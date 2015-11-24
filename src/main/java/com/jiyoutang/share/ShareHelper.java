package com.jiyoutang.share;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by tianlong on 2015/10/12.
 */
public class ShareHelper {
    private static UMSocialService mController;
    //QQ
    private static String mQqKeyID;//
    private static String mQqKeySecret;//
    //微信
    private static String mWxAppID;//
    private static String mWxAppSecret;//

    private static boolean mNeedShareUseWeb = false;


    public static String getmQqKeyID() {
        return mQqKeyID;
    }

    public static String getmQqKeySecret() {
        return mQqKeySecret;
    }

    public static String getmWxAppID() {
        return mWxAppID;
    }


    public static String getmWxAppSecret() {
        return mWxAppSecret;
    }

    /**
     * @param packageName app的应用包名
     * @功能描述 : 初始化分享的包名
     */
    public static void init(String packageName) {
        mController = UMServiceFactory.getUMSocialService(packageName);
    }

    /**
     * @param qqKeyID     QQ分享需要用到的key
     * @param qqKeySecret qq分享用到的secret
     * @功能描述 : 初始化分享QQ的key
     */
    public static void initQQConfig(String qqKeyID, String qqKeySecret){
        mQqKeyID = qqKeyID;
        mQqKeySecret = qqKeySecret;
    }

    /**
     * 对外透漏设置调试模式（是否带log）接口
     */
    public static void setDebug(boolean debug) {
        LogUtils.setmPrintLog(debug);
    }

    /**
     * @功能描述 : 分享的界面的onActivityResult需要回调此方法
     */
    public static void onShareActivityResult (int requestCode, int resultCode, Intent data) {
        //如果有使用任一平台的SSO授权或者集成了facebook平台,
        // 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        LogUtils.d("ssoHandler-------->" + ssoHandler);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        } else {
            if (mController != null)
                mController.getConfig().cleanListeners();
        }
    }
    /**
     * 开始分享
     * @param shareSetting 分享的用户设置
     *
     * @功能描述 : 添加新浪平台
     */
    public static void startShare(ShareSetting shareSetting) {
        addQQPlatform(shareSetting.getmActivity(), mQqKeyID, mQqKeySecret);
        addWXPlatform(shareSetting.getmActivity(), mWxAppID, mWxAppSecret);
        addSINAPlatform(shareSetting.getmActivity());
        setShareContent(shareSetting);
        // 首先在您的Activity中添加如下成员变量
        // 设置分享内容
        mController.setShareContent(shareSetting.getShareContent());
        // 设置分享图片, shareH5图片的url地址
        mController.setShareMedia(shareSetting.getShareImage());
        //关掉友盟的toast提示
        mController.getConfig().closeToast();
        CustomShareBoard customShareBoard = new CustomShareBoard(shareSetting.getmActivity(), mController);
        if (shareSetting.getActionReportListener() != null){
            customShareBoard.setActionReportListener(shareSetting.getActionReportListener());
        }
    }
    /**
     * @param wxAppID     微信分享需要用到的key
     * @param wxAppSecret 微信分享用到的secret
     * @功能描述 : 初始化分享微信的key
     */
    public static void initWXConfig(String wxAppID, String wxAppSecret){
        mWxAppID = wxAppID;
        mWxAppSecret = wxAppSecret;
    }

    /**
     * @param activity    注册需要用到的界面元素
     * @param qqKeyID     QQ分享需要用到的key
     * @param qqKeySecret qq分享用到的secret
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    public static void addQQPlatform(Activity activity, String qqKeyID, String qqKeySecret) {
        // 天天象上，腾讯开放平台
        // 添加QQ支持, 并且设置QQ分享内容的target url
        // 注意在授权前先检查是否已经授权过，重复授权有可能引起错误
        if (!OauthHelper.isAuthenticated(activity, SHARE_MEDIA.QQ)) {
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, qqKeyID, qqKeySecret);
            qqSsoHandler.addToSocialSDK();
        }
        // 添加QZone平台
        // 注意在授权前先检查是否已经授权过，重复授权有可能引起错误
        if (!OauthHelper.isAuthenticated(activity, SHARE_MEDIA.QZONE)) {
            QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, qqKeyID, qqKeySecret);
            qZoneSsoHandler.addToSocialSDK();
        }
    }


    /**
     * @param activity    注册需要用到的界面元素
     * @param wxAppID     微信分享需要用到的key
     * @param wxAppSecret 微信分享用到的secret
     * @功能描述 : 添加微信平台分享
     */
    private static void addWXPlatform(Activity activity, String wxAppID, String wxAppSecret) {
        // 注意：在微信授权的时候，必须传递appSecret
        // 添加微信平台
        // 注意在授权前先检查是否已经授权过，重复授权有可能引起错误
        if (!OauthHelper.isAuthenticated(activity, SHARE_MEDIA.WEIXIN)) {
            UMWXHandler wxHandler = new UMWXHandler(activity, wxAppID, wxAppSecret);
            wxHandler.addToSocialSDK();
        }
        // 添加微信朋友圈
        // 注意在授权前先检查是否已经授权过，重复授权有可能引起错误
        if (!OauthHelper.isAuthenticated(activity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            UMWXHandler wxCircleHandler = new UMWXHandler(activity, wxAppID, wxAppSecret);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();
        }
    }

    /**
     * * @return
     *
     * @功能描述 : 添加新浪平台
     */
    private static void addSINAPlatform(Activity activity) {
        //添加新浪平台
        //设置新浪SSO handler
        // 注意在授权前先检查是否已经授权过，重复授权有可能引起错误
        if (!OauthHelper.isAuthenticated(activity, SHARE_MEDIA.SINA)) {
            mController.getConfig().setSsoHandler(new SinaSsoHandler());
        }
        //添加回调地址
        mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
    }



    public static boolean ismNeedShareUseWeb() {
        return mNeedShareUseWeb;
    }

    public static void setmNeedShareUseWeb(boolean mNeedShareUseWeb) {
        ShareHelper.mNeedShareUseWeb = mNeedShareUseWeb;
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private static void setShareContent(ShareSetting shareSetting) {
        String shareH5 = shareSetting.getShareUrl();
        String shareContent = shareSetting.getShareContent();
        String shareTitle = shareSetting.getShareTitle();
        UMImage umImage = shareSetting.getShareImage();
        String shareSinaFrom = shareSetting.getShareSinaFrom();
        String shareSinaAttention = shareSetting.getShareSinaAttention();

        if (umImage != null) {
            umImage.setTargetUrl(shareH5);
        }
        //1.设置微信平台分享
        //设置微信分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareH5);
        weixinContent.setShareMedia(umImage);
        mController.setShareMedia(weixinContent);

        //2.设置朋友圈分享
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(shareTitle);
        circleMedia.setTargetUrl(shareH5);
        circleMedia.setShareMedia(umImage);
        mController.setShareMedia(circleMedia);

        //3.设置qq平台分享
        //设置QQ分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setTargetUrl(shareH5);
        qqShareContent.setShareMedia(umImage);
        mController.setShareMedia(qqShareContent);

        //4.设置QQ空间平台分享
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareContent);
        qzone.setTargetUrl(shareH5);
        qzone.setTitle(shareTitle);
        qzone.setShareMedia(umImage);
        mController.setShareMedia(qzone);

        //5.设置新浪微博平台分享
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(shareSinaAttention);
        stringBuffer.append(shareContent);
        stringBuffer.append(shareH5);
        stringBuffer.append(shareSinaFrom);
        //设置新浪微博分享内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(stringBuffer.toString());
        sinaContent.setShareImage(umImage);
        sinaContent.setTitle(shareTitle);
        sinaContent.setTargetUrl(shareH5);

        mController.getConfig().setDefaultShareLocation(false);
        mController.setShareMedia(sinaContent);
    }

}
