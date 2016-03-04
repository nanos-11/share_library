# share_library
share_library


git clone git@github.com:nanos-11/share_library.git

分享模块


集成说明
对外API
1.	分享类ShareHelper
ShareHelper.init(String packageName);
方法说明：
初始化应用包名
参数说明：
packageName当前应用的包名  // com.jiyoutang.scanissue
使用说明：
在应用所有分享接口前调用

ShareHelper.initQQConfig(String qqKeyID, String qqKeySecret)
方法说明：
初始化QQ相关Key
参数说明：
qqKeyID      QQ的appId  // wx2d140b79c9b16d2f
qqKeySecret   QQ的appSecret  // wx2d140b79c9b16d2f
使用说明：
在应用所有分享接口前调用

ShareHelper.initWXConfig(String wxAppID, String wxAppSecret)
方法说明：
初始化WX相关Key
参数说明：
wxAppID      QQ的appId      // wx2d140b79c9b16d2f
wxAppSecret   QQ的appSecret  // wx2d140b79c9b16d2f
使用说明：
在应用所有分享接口前调用

PS：新浪
特殊说明
新浪的key需要在友盟后台进行设置

ShareHelper. setDebug(boolean debug)
方法说明：
初始化调试模式
参数说明：
debug    是否需要log      
使用说明：
在应用所有分享接口前调用

ShareHelper. setmNeedShareUseWeb(boolean mNeedShareUseWeb)
方法说明：
是否允许网页分享
参数说明：
mNeedShareUseWeb 是否允许网页分享（QQ&WX）      
使用说明：
在应用所有分享接口前调用

ShareHelper. startShare(ShareSetting shareSetting)
方法说明：
开始分享
参数说明：
shareSetting 分享对象      
使用说明：
分享时候使用


2.	分享对象ShareSetting
ShareSetting shareSetting = new ShareSetting(Activity activity);
方法说明：
构造分享对象
参数说明：
	Activity 分享当前界面元素
使用说明：
分享对象的时候需要构造

shareSetting.setShareTitle(String shareTitle)
方法说明：
构造分享对象的标题
参数说明：
shareTitle分享对象的标题
使用说明：
分享对象的时候需要设置

shareSetting.setShareImg(String shareImgUrl)
shareSetting.setShareImg(int shareImgSourceId)
shareSetting.setShareImg(File shareImgFile)
shareSetting.setShareImg(byte[] shareImgData)
shareSetting.setShareImg(Bitmap shareImg)
方法说明：
构造分享对象的图片
参数说明：
shareImgUrl分享对象的图片元素属性 默认程序启动图
使用说明：
分享对象的时候需要设置

shareSetting. setShareUrl(String shareUrl)
方法说明：
构造分享对象的网络url
参数说明：
shareUrl分享对象的网络url
使用说明：
分享对象的时候需要设置

shareSetting. setShareContent(String shareContent)
方法说明：
构造分享对象的内容
参数说明：
shareContent分享对象的内容
使用说明：
分享对象的时候需要设置

shareSetting. setShareSinaFrom(String shareSinaFrom)
方法说明：
构造分享对象的新浪分享需要用到的来自
参数说明：
shareSinaFrom分享对象的新浪分享需要用到的来自 // (来自天天扫题)
使用说明：
分享对象的时候需要设置

shareSetting. setShareSinaAttention(String shareSinaAttention)
方法说明：
构造分享对象的新浪分享需要用到的新浪关注号
参数说明：
shareSinaAttention分享对象的新浪分享需要用到的新浪关注号 // (来自天天扫题)
使用说明：
分享对象的时候需要设置

shareSetting. setMarginleftright(int marginleftright)
方法说明：
构造分享对话框的到手机屏幕的边距（默认86px，左右各一个marginleftright）
参数说明：
marginleftright 分享对话框的到手机屏幕的边距 // "#天天象上官微#"
使用说明：
分享对象的时候需要设置

shareSetting. setBackGroundAlpha(float alpha) 
方法说明：
构造分享对话框的透明度（默认50%）
参数说明：
alpha分享对话框的透明度
使用说明：
分享对象的时候需要设置

shareSetting. setActionReportListener(ActionReportListener listener) 
方法说明：
设置分享对话框中分享渠道的点击事件
参数说明：
listener分享对话框的分享渠道的点击事件
构造说明：
public interface ActionReportListener {
    void setActionReportListenerWECHAT();
    void setActionReportListenerWECHATFRIENDS();
    void setActionReportListenerQQ();
    void setActionReportListenerQQZONE();
    void setActionReportListenerSINAWEIBO();
    void setActionReportListenerSHARESUCCEED();
    void setActionReportListenerSHAREFAILED();
}
使用说明：
数据埋点使用

	混淆
#友盟分享
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-libraryjars libs/SocialSDK_QQZone_2.jar
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class <PackageName>.R$*{
    public static final int *;
}
	AndroidManifest.xml
1、	需要把lib中的WXEntryActivity 的包名改成<PackageName>.wxapi；
2、	在androidManifest中注册WXEntryActivity：
<!—注册微信分享的回调类， -->
<activity
android:name=".wxapi.WXEntryActivity"
android:configChanges="keyboardHidden|orientation|screenSize"
android:exported="true"
android:screenOrientation="portrait"
android:theme="@android:style/Theme.Translucent.NoTitleBar"/>


