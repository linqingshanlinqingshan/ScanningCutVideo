package com.example.administrator.retrofitmvp;


import android.os.Environment;

public class BriefStoreConfig {

    public static boolean isBebug = true;
    public static String SP_FILE_NAME = "sp_briefStore";
    public static String SP_LOGIN_FILE = "sp_login_file";
    public static String SP_LONGIN_DEFAULT = "sp_login_default";
    public static String SP_KEY_LOGIN_DEFAULT_ACCOUNT = "sp_key_login_default_account";
    public static String SP_KEY_LOGIN_DEFAULT_PASSWORD = "sp_key_login_default_password";
    public static String SP_KEY_USERID = "sp_key_userid";

    public static String SP_KEY_TOKEN = "sp_key_token";


    // 服务器接口地址
//   public static String URL_BASE = "https://www.thethinking.cc/CaseLibServer/Gani_shop_webapi/";
    public static String URL_BASE = "https://shopapp.gani.com.cn/Gani_shop_webapi/";

    // 图片地址
//   public static String URL_IMAGE = "https://www.thethinking.cc/CaseLibServer/";
    public static String URL_IMAGE = "https://shopapp.gani.com.cn/";

    //   public static String URL_BASEV3 = "https://www.thethinking.cc/CaseLibServer/v3/webapi/";
//   public static String URL_BASEV3 = "https://shopapp.gani.com.cn/Gani_shop_webapi/";
    public static String URL_BASEV3 = "https://shopapp.gani.com.cn/v3/webapi/";
//   public static String URL_BASEV3 = "https://shopapp.gani.com.cn/CaseLibServer/v3/webapi/";

    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + MvpApplication.getInstance().getResources().getString(R.string.briefstore_directory2);
//   public static String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/bridfstore/";

    public static final int LIMIT_NUM_HOMECAMP = 5;
    public static final int LIMIT_NUM_HOMEMENU = 5;


    public static final String DIR_ROOT = "" + Environment.getExternalStorageDirectory();
    public static final String DIR_IMAGE = DIR_ROOT;
    public static final String AUDIO_RECORD = DIR_ROOT + "/audio/";
    public static final String DIR_IMAGE_COMPRESS = DIR_IMAGE + "/compress/";


}
