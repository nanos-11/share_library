package com.jiyoutang.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

import java.io.File;


/**
 * 【需求来源】：PR-V1.2-0101 	分享功能入口：习题讲解页
 * 	点击分享按钮后，弹出分享渠道选择框，点击分享选择框以外的其他区域，关闭分享选择框。Android用户点击手机硬件返回键，关闭分享选择框
 * 	分享通道以此顺序为：“微信好友”、“微信朋友圈”、“QQ好友”、“QQ空间”、“新浪微博”
 * 	点击各渠道，进入相应渠道APP应用进行分享，如果手机内没有安装相应应用，则打开相应应用的网页页面
 * 	没有网络时，点击各通道，弹出toast
 * 	当分享请求超时（30s）或分享失败时，自动返回至天天扫题客户端，并弹出toast“分享失败”
 * <p/>
 * 【修改人】：nanjinglong@jiyoutang.com
 * 【修改时间】：2015-08-4
 */
public class ShareSetting {
    private Activity mActivity;
    private Context mContext;
    private static int MARGINLEFTRIGHT = 86 * 2;//设置默认边距; //左右外边距
    private static float ALPHA = 0.5f;//设置默认背景透明度;  //设置背景透明度
    private UMImage shareImage;//分享用到的图片（传进来的图片地址--视频封面图片 各个分享平台共用）  ===》本地图片分享方式
    private String shareContent; //分享内容的一个集合
    private String shareSinaFrom;//设置新浪分享的来自
    private String shareSinaAttention;//设置新浪分享的##关注号
    private UMSocialService mController;//社会化服务
    private String shareUrl; //分享的H5 url
    private String shareTitle;//分享的标题
    private ActionReportListener listener; // 分享点击事件，用来添加行为点

    /**
     * 分享
     *
     * @param activity 分享用到的界面元素
     */
    public ShareSetting(Activity activity) {
        this.mActivity = activity;
        shareImage = new UMImage(mActivity, R.mipmap.ic_launcher);//设置默认分享图片
    }


    public Activity getmActivity() {
        return mActivity;
    }
    /**
     * 标题
     */
    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareTitle () {
        return shareTitle;
    }

    public UMImage getShareImage() {
        return shareImage;
    }
    /**
     * 设置分享 网络图片
     *
     * @param shareImgUrl 分享网络url的网络连接地址
     */
    public void setShareImg(String shareImgUrl) {
        shareImage = new UMImage(mContext, shareImgUrl);
    }

    /**
     * 设置分享 本地工程图片
     *
     * @param shareImgSourceId 工程图片资源id
     */
    public void setShareImg(int shareImgSourceId) {
        shareImage = new UMImage(mContext, shareImgSourceId);
    }

    /**
     * 设置分享 本地物理图片
     *
     * @param shareImgFile 本地物理图片文件
     */
    public void setShareImg(File shareImgFile) {
        shareImage = new UMImage(mContext, shareImgFile);
    }

    /**
     * 设置分享 图片二进制数据
     *
     * @param shareImgData 图片二进制数据
     */
    public void setShareImg(byte[] shareImgData) {
        shareImage = new UMImage(mContext, shareImgData);
    }

    /**
     * 设置分享 图片原图
     *
     * @param shareImg 图片原图
     */
    public void setShareImg(Bitmap shareImg) {
        shareImage = new UMImage(mContext, shareImg);
    }


    /**
     * 设置分享的h5
     *
     * @param shareUrl
     */
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }
    /**
     * 设置分享内容
     *
     * @param shareContent
     */
    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareContent() {
        return shareContent;
    }

    /**
     * 设置新浪分享的来自
     *
     * @param shareSinaFrom 分享新浪内容中的来自
     */
    public void setShareSinaFrom(String shareSinaFrom) {
        this.shareSinaFrom = shareSinaFrom;
    }

    public String getShareSinaFrom() {
        return shareSinaFrom;
    }

    /**
     * 设置新浪分享的关注号
     *
     * @param shareSinaAttention 分享新浪内容中的关注号
     */
    public void setShareSinaAttention(String shareSinaAttention) {
        this.shareSinaAttention = shareSinaAttention;
    }

    public String getShareSinaAttention() {
        return shareSinaAttention;
    }

    /**
     * 设置左右边距  默认边距是 86 * 2
     *
     * @param marginleftright
     */
    public void setMarginleftright(int marginleftright) {
        this.MARGINLEFTRIGHT = marginleftright * 2;
    }

    /**
     * 设置背景透明度 否则默认显示透明度为50%
     *
     * @param alpha
     */
    public void setBackGroundAlpha(float alpha) {
        this.ALPHA = alpha;
    }

    public void setActionReportListener(ActionReportListener listener) {
        this.listener = listener;
    }

    public ActionReportListener getActionReportListener() {
        return this.listener;
    }
}
