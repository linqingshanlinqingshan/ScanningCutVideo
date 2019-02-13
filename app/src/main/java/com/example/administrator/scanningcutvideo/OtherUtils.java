package com.example.administrator.scanningcutvideo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/20.
 */

public class OtherUtils {

    private static final String TAG = "----->OtherUtils";

    /**
     * 将传进去的字符串参数格式化打印
     *
     * @param maps
     * @return
     */
    public static String ruleLog(Map<String, String> maps) {
        String tempStr = "";
        for (Map.Entry entry : maps.entrySet()) {
            String keyStr = (String) entry.getKey();
            String valueStr = (String) entry.getValue();
            tempStr = tempStr + keyStr + " = " + valueStr + "\n";
        }
        return tempStr;
    }

    public static String tagMapToString(Map<Integer, String> map) {
        //136;130;135;133
        String temp = "";
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            temp += entry.getKey() + ";";
        }

        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    /**
     * map转为json数据
     *
     * @param map
     * @return
     */
    public static String mapToJsonStr(Map<String, List<String>> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Gson gson = new Gson();

        return gson.toJson(map);
    }

    /**
     * 获取分秒显示
     *
     * @param duration
     * @return
     */
    public static String getMS(long duration) {
        int second = (int) (duration / 1000);
        int minute = second / 60;
        second %= 60;
        String m, s;
        if (minute / 10 == 0)
            m = "0" + minute;
        else
            m = minute + "";
        if (second / 10 == 0)
            s = "0" + second;
        else
            s = second + "";

        return m + ":" + s;
    }

    public static void copyText(Context context, String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
        Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show();
    }

    /**
     * 字符串截取变红
     *
     * @param string3 你要变色的字符，可为null
     * @param string2 你要变色的字符
     * @param string  整个字符串
     * @param numtext
     */
    public static void stringInterceptionChangeRed(TextView numtext,
                                                   String string, String string2, String string3) {
        int fstart = string.indexOf(string2);
        int fend = fstart + string2.length();
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        if (!"".equals(string3) && string3 != null) {
            int bstart = string.indexOf(string3);
            int bend = bstart + string3.length();
            style.setSpan(new ForegroundColorSpan(Color.RED), bstart, bend,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        if (numtext != null) {
            numtext.setText(style);
        }
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    protected static final int LOCATION_REQUEST_CODE = 101;                                         //设置GPS的返回码


    /**
     * 是否打开了GPS 没打开则提示
     *
     * @param activity
     * @return
     */
    public static boolean initGPS(final Activity activity) {

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            LogUtil.logDebug(TAG, "---> 判断GPS模块是否开启，如果没有则开启");

            final View view = LayoutInflater.from(activity).inflate(R.layout.open_gps_layout, null);
            final TextView tv_title = view.findViewById(R.id.tv_title);
            final TextView tv_cancel = view.findViewById(R.id.tv_cancel);
            final TextView tv_setting = view.findViewById(R.id.tv_setting);

            tv_title.setText(activity.getResources().getString(R.string.open_gps));

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);             //点击屏幕不消失

            final AlertDialog alertDialog = dialog.create();

            //点击外面不消失
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(true);

            alertDialog.show();

            alertDialog.getWindow().setContentView(view);

            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    LogUtil.logDebug(TAG, "---> onDismiss");  //监听返回键
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.logDebug(TAG, "---> 取消");
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });

            tv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.logDebug(TAG, "---> 设置");
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(intent, LOCATION_REQUEST_CODE); // 设置完成后返回到原来的界面
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });

            return false;

        } else {
            LogUtil.logDebug(TAG, "---> GPS模块已开启");
            return true;
        }
    }


    //DecimalFormat df = new DecimalFormat("#");//保留小数点后0位  df.format((Double) my_contract_amount_count)

//键盘
//    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);    //InputMethodManager.SHOW_FORCED

}
