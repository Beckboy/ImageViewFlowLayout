package com.example.hunter_j.imageviewflowlayout.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hunter_J on 17/3/30.
 */

public class ToastUtil {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;

    private static ToastUtil instance;

    /**
     * Toast对象
     */
    private static Toast toast = null;

    /**
     * 上一次toast的时间
     */
    private static long oldTime = 0;

    /**
     * 本次toast的时间
     */
    private static long newTime = 0;

    private ToastUtil(Context context) {
        toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    public static ToastUtil getInstance(Context context) {
        if (null == instance) {
            synchronized (ToastUtil.class) {
                if (null == instance) {
                    instance = new ToastUtil(context);
                }
            }
        }
        return instance;
    }

    public ToastUtil setContent(String content){
        toast.setText(content);
        return instance;
    }

    public ToastUtil setDuration(int duration){
        toast.setDuration(duration);
        return instance;
    }

    public ToastUtil setGravity(int gravity,int xOffset,int yOffset){
        toast.setGravity(gravity,xOffset,yOffset);
        return instance;
    }

    public void setShow(){
        if (null == toast) return;
        toast.show();
    }

    public void setHint(){
        if (null == toast) return;
        toast.cancel();
    }

    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            oldTime = System.currentTimeMillis();
        } else {
            newTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (newTime - oldTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oldTime = newTime;
    }

}
