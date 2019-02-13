package com.example.administrator.retrofitmvp;

import android.widget.Toast;


public class ToastUtil {

    private static Toast toast = null;

    public static void showToastSHORT(String text) {
        if (toast == null) {
            toast = Toast.makeText(MvpApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    public static void showToastLONG(String text) {
        if (toast == null) {
            toast = Toast.makeText(MvpApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
