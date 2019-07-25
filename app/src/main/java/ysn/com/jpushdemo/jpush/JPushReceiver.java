package ysn.com.jpushdemo.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lazy.library.logging.Logcat;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

import cn.jpush.android.api.JPushInterface;
import ysn.com.jpushdemo.MainActivity;
import ysn.com.jpushdemo.model.event.TextEvent;

/**
 * @Author yangsanning
 * @ClassName JPushReceiver
 * @Description 自定义接收器
 * @Date 2019/7/25
 * @History 2019/7/25 author: description:
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    public static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            String msg = printBundle(bundle);
            Logcat.d().tag(TAG).ln().ln()
                    .msg("---- " + intent.getAction() + " Print Start ----").ln()
                    .msg(msg).ln().ln()
                    .msg("---- " + intent.getAction() + " Print End ----").ln()
                    .out();
            EventBus.getDefault().post(new TextEvent(msg));

            switch (Objects.requireNonNull(intent.getAction())) {
                case JPushInterface.ACTION_CONNECTION_CHANGE:
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    Logcat.w().tag(TAG).msg("连接动态改变 ").msg(intent.getAction() + " connected state change to " + connected).out();
                    break;
                case JPushInterface.ACTION_REGISTRATION_ID:
                    // 将注册ID发送到服务器
                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                    Logcat.d().tag(TAG).msg("接收到的注册 Id: " + regId).out();
                    break;
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Logcat.d().tag(TAG).msg("接收到自定义消息").out();
                    Logcat.d().tag(TAG).msg("message: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)).out();

//                    processCustomMessage(context, bundle);
                    break;
                case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                    Logcat.d().tag(TAG).msg("接收到通知").ln().out();
                    Logcat.d().tag(TAG).msg("notificationId: " + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)).out();
                    break;
                case JPushInterface.ACTION_NOTIFICATION_OPENED:
                    Logcat.d().tag(TAG).msg("用户点击了通知").out();
                    //打开自定义的Activity
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
                case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                    Logcat.d().tag(TAG).msg("用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)).out();
                    break;
                default:
                    Logcat.d().tag(TAG).msg("Unhandled intent - " + intent.getAction()).out();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印所有的 intent extra 数据
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nKey: ").append(key).append(", Value: ").append(bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nKey: ").append(key).append(", Value: ").append(bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Logcat.i().tag(TAG).msg("This message has no Extra data").out();
                        continue;
                    }

                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();
                        while (it.hasNext()) {
                            String myKey = it.next();
                            sb.append("\nKey: ").append(key)
                                    .append(", Value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        }
                    } catch (JSONException e) {
                        Logcat.i().tag(TAG).msg("Get message extra JSON error!").out();
                    }
                    break;
                default:
                    sb.append("\nKey: ").append(key).append(", Value: ").append(bundle.get(key));
                    break;
            }
        }
        return sb.toString();
    }
}

