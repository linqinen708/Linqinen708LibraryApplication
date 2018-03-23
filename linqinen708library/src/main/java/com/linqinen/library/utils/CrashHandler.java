package com.linqinen.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类   
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例  
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象  
    private Context mContext;
    //用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//    	httpPost(ex.toString());
//        if (!(ex) && mDefaultHandler != null) {
//            //如果用户没有处理则让系统默认的异常处理器来处理
//            Log.i("uncaughtException", "uncaughtException");
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.i("exit", "exit");
////           mDefaultHandler.uncaughtException(thread, ex);
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        ex.printStackTrace();
            LogT.i("崩溃了:" );
        handleException(ex);
//        }

        if(mDefaultHandler !=null){
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        Log.i(TAG, "handleException: " + "run");
        if (ex == null) {
            return false;
        }
//        使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(mContext, "很抱歉,程序出现异常,即将自动退出", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();

        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        String saveFilePath;
        saveFilePath = saveCrashInfo2File(ex);
        LogT.i("崩溃错误日志地址saveFilePath:" + saveFilePath);
//        httpPost(saveFilePath);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "在收集程序信息时发生了错误", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "在收集程序信息时发生了错误", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
//        httpPost(sb.toString());
        try {
//            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time /*+ timestamp*/ + ".txt";
            File file = new File(getCacheFolder("crash", mContext), fileName);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = new FileOutputStream(file);
                Log.i("path", file.getPath());
                Log.i("sb", sb.toString());
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return file.getPath();
        } catch (Exception e) {
            Log.e(TAG, "在写入文件时发生了错误", e);
        }
        return null;
    }

    public File getCacheFolder(String name, Context context) {
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "android" + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "cache", name);

        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
/*
    private void httpPost(final String saveFilePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json_post = new JSONObject();
                    json_post.put("id", 0);//发送人id
                    json_post.put("content", "系统异常");//1:好友消息 2 系统消息

                    MultipartEntity myEntity = new MultipartEntity();

                    if (saveFilePath != null) {
                        myEntity.addPart("api_files", new FileBody(new File(saveFilePath), ".log"));
                    }

                    String result = MyHttpClient.getInstance().mHttpPost(Data.serverIP + "/wake/user/feedback",TAG,json_post,myEntity);

                    if(result != null){
                        JSONObject jsonObject_result = new JSONObject(result);
                        String code = jsonObject_result.getString("code");
                        if ("1".equals(code)) {

                        }else{
                            final String errorMsg = jsonObject_result.getString("errorMsg");
                            Log.i(TAG, "run: errorMsg:"+errorMsg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
