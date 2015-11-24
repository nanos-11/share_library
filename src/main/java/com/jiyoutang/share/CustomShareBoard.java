/**
 *
 */

package com.jiyoutang.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.Map;
import java.util.Set;


/**
 * 自定义编辑界面 nan
 */

/**
 * 【BUG来源】：华为H60机型  继承PopupWindow不显示背景  改成了用Dialog
 * 【修改人】：nanjinglong@jiyoutang.com
 * 【修改时间】：2015-09-18
 */
public class CustomShareBoard implements View.OnClickListener {

    private final UMSocialService mController;
    private Activity mActivity;
    private UMWXHandler wxHandler;
    private UMQQSsoHandler qqSsoHandler;
    private static final int ECODE = 40000;//返回码
    private View view;
    private MyDialog builder;
    private ActionReportListener actionReportListener;

    public ActionReportListener getActionReportListener() {
        return actionReportListener;
    }

    public void setActionReportListener(ActionReportListener actionReportListener) {
        this.actionReportListener = actionReportListener;
    }

    public CustomShareBoard(Activity activity, UMSocialService mController) {
        this.mController = mController;
        this.mActivity = activity;
        initView();
    }


    private void initView() {
        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_share_teacher, null);
        view.findViewById(R.id.share_text1).setOnClickListener(this);
        view.findViewById(R.id.share_text2).setOnClickListener(this);
        view.findViewById(R.id.share_text3).setOnClickListener(this);
        view.findViewById(R.id.share_text4).setOnClickListener(this);
        view.findViewById(R.id.share_text5).setOnClickListener(this);
        showDialog(mActivity, view);
    }

    @Override
    public void onClick(View v) {
        if (mActivity == null) {
            if (builder != null) {
                builder.dismiss();
            }
            return;
        }
        if (!isConnected(mActivity)) {
            Toast.makeText(mActivity, R.string.error_net, Toast.LENGTH_SHORT).show();
            if (builder != null) {
                builder.dismiss();
            }
            return;
        }
        int id = v.getId();

        /**
         * 【BUG来源】：BUG-V1.2-0010750 	设备已安装qq后，点击分享qq好友或空间，提示分享失败
         * 【参考需求】：UE-V1.2
         * 【报告员】：anjingrong@jiyoutang.com
         * 【修改人】：nanjinglong@jiyoutang.com
         * 【bug原因】：逻辑不完善
         * 【修改时间】：2015-08-12
         */
        wxHandler = new UMWXHandler(mActivity, ShareHelper.getmWxAppID(), ShareHelper.getmWxAppSecret());
        qqSsoHandler = new UMQQSsoHandler(mActivity, ShareHelper.getmQqKeyID(), ShareHelper.getmQqKeySecret());

        //问题的原因=====>是Android library中生成的R.java中的资源ID不是常数
        //查找原因====>打开library中的R.java，每一个资源ID都没有被声明为final
        //解决方案=====>>>在library中通过if-else-if条件语句来引用资源ID
        if (id == R.id.share_text1) {
            if (actionReportListener != null) {
                actionReportListener.setActionReportListenerWECHAT();
            }
            if (!wxHandler.isClientInstalled()) {
                Toast.makeText(mActivity, "你还没有安装微信", Toast.LENGTH_SHORT).show();
            } else {
                performShare(SHARE_MEDIA.WEIXIN);
            }
        } else if (id == R.id.share_text2) {
            if (actionReportListener != null)
                actionReportListener.setActionReportListenerWECHATFRIENDS();
            if (!wxHandler.isClientInstalled()) {
                Toast.makeText(mActivity, "你还没有安装微信", Toast.LENGTH_SHORT).show();
            } else {
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        } else if (id == R.id.share_text3) {
            if (actionReportListener != null) {
                actionReportListener.setActionReportListenerQQ();
            }
            //判断 是否安装了QQ客户端 true:未安装走网页登录 false：弹出提示
            if (ShareHelper.ismNeedShareUseWeb()) {
                performShare(SHARE_MEDIA.QQ);
            } else {
                if (!qqSsoHandler.isClientInstalled()){
                    Toast.makeText(mActivity, "你还没有安装QQ", Toast.LENGTH_SHORT).show();
                } else {
                    performShare(SHARE_MEDIA.QQ);
                }
            }
        } else if (id == R.id.share_text4) {
            if (actionReportListener != null) {
                actionReportListener.setActionReportListenerQQZONE();
            }
            //判断是否安装了QQ客户端true:未安装走网页登录 false：弹出提示
            if (ShareHelper.ismNeedShareUseWeb()) {
                performShare(SHARE_MEDIA.QZONE);
            } else {
                if (!qqSsoHandler.isClientInstalled()) {
                    Toast.makeText(mActivity, "你还没有安装QQ", Toast.LENGTH_SHORT).show();
                } else {
                    performShare(SHARE_MEDIA.QZONE);
                }
            }
        } else if (id == R.id.share_text5) {
            if (actionReportListener != null) {
                actionReportListener.setActionReportListenerSINAWEIBO();
            }
            //判断是否安装了QQ客户端true:未安装走网页登录 false：弹出提示
            performShare(SHARE_MEDIA.SINA);
        }
        if (builder != null) {
            builder.dismiss();
        }
    }

    private void performShare(SHARE_MEDIA platform) {

        /**
         * 【BUG来源】：BUG-V1.2-0010747 打开分享渠道选框，锁屏，解锁后点击qq分享后程序崩溃
         * 【参考需求】：???
         * 【报告员】：anjingrong@jiyoutang.com
         * 【修改人】：nanjinglong@jiyoutang.com
         * 【bug原因】：逻辑不完善对监听进行清理
         * 【修改时间】：2015-08-12
         */
        /**
         * 每次分享注册监听
         * 分享后会取消监听 然后重新注册
         */
        mController.registerListener(new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            }
        });

        /**
         * 参数1为Context类型对象， 参数2为要分享到的目标平台， 参数3为分享操作的回调接口
         */
        mController.postShare(mActivity, platform,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        //LogUtils.d("开始分享=========>onStart");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                        if (eCode == 200) {
                            //LogUtils.d("------->------->分享成功");
                            Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();
                            if (actionReportListener != null)
                                actionReportListener.setActionReportListenerSHARESUCCEED();
                        } else {
                            String eMsg = "";
                            if (eCode == -101) {
                                eMsg = "没有授权";
                            }
                            Log.d("TAG", "分享失败------->" + eCode + "]----------->" + eMsg);
                            if (eCode == ECODE) {
                                if (actionReportListener != null)
                                    actionReportListener.setActionReportListenerSHAREFAILED();
                            } else {
                                Toast.makeText(mActivity, "分享失败", Toast.LENGTH_SHORT).show();
                                if (actionReportListener != null)
                                    actionReportListener.setActionReportListenerSHAREFAILED();
                            }
                        }
                        //分享的回调会被调用多次 主动取消注册
                        mController.unregisterListener(this);
//                        dismiss();
                    }
                });


        /**
         * 不调起 编辑页直接分享
         */
//        mController.directShare(mActivity, platform,
//                new SocializeListeners.SnsPostListener() {
//                    @Override
//                    public void onStart() {}
//
//                    @Override
//                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {}
//                });
        /**
         * 获取平台数据开始
         */
        mController.getPlatformInfo(mActivity, platform, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
                Log.d("TAG", "onStart---获取平台数据开始...");
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    StringBuilder sb = new StringBuilder();
                    Set<String> keys = info.keySet();
                    for (String key : keys) {
                        sb.append(key + "=" + info.get(key).toString() + "\r\n");
                    }
                    Log.d("TAG", "onComplete" + sb.toString());
                } else {
                    Log.d("TAG", "onComplete----发生错误：没有授权" + status);
                }
            }
        });

//        if (mController != null) {
//            Log.d("TAG", "-->cleanListeners");
//            mController.getConfig().cleanListeners();
//        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connectivity) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (null != info && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public class MyDialog extends Dialog {
        public MyDialog(Context context, int width, View layout, int style) {
            super(context, style);
            setContentView(layout);
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = width;
            window.setAttributes(params);
        }
    }

    /**
     * 定义对话框样式
     *
     * @param activity
     * @param view
     */
    private void showDialog(Activity activity, View view) {
        int width = dip2px(activity, 274);
        builder = new MyDialog(activity, width, view, R.style.dialog);
        //可以触摸和返回   例：删除
        builder.setCanceledOnTouchOutside(true);
        builder.setCancelable(true);
        if (!activity.isFinishing())
            builder.show();
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
