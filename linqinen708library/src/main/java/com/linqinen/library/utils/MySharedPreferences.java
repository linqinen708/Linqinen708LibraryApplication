//package com.linqinen.library.utils;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
///**
// * Created by lin on 2016/11/24.
// */
//
//public class MySharedPreferences {
//
//
//    private SharedPreferences mSharedPreferences;
//    private Context mContext;
//
//    private boolean wakeUp1Password;
//    private String cityListString;
//
//
//    /**
//     * 内部类实现单例模式
//     * 延迟加载，减少内存开销
//     */
//    private static class SingletonHolder {
//        private static MySharedPreferences instance = new MySharedPreferences();
//    }
//
//    public static MySharedPreferences getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    private MySharedPreferences(){
//        LogT.i("初始化MySharedPreferences");
////        if (mContext != null) {
////            mSharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
////        }
//    }
//
//    public void init(Context context){
//        LogT.i("初始化init:"+context);
//        mContext = context;
//        if (mContext != null) {
//            mSharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
//        }
//    }
//
////    /**初始化本地的缓存数据*/
////    public void initSharedPreferencesData(){
////        setAccessToken(null);
////        setAutoLogin(false);
////        setUserId(null);
////        setMobile(null);
////        setRegistered(false);
////        setWakeUpSwitch(false);
////    }
//
//    /**
//     * 清除本地数据,accessToken不能为null，需要为""，否则有错误
//     */
//    public void clearData() {
//            mSharedPreferences.edit().clear().apply();
////        if (mSharedPreferences != null) {
////        }else{
////            LogT.i("mSharedPreferences为空");
////        }
//    }
//
////    public void deleteSharedPreference(Context context) {
////        HomeActivity.accessToken = "";
////        HomeActivity.autoLogin = false;
//////        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
////        deleteFilesByDirectory(new File(context.getFilesDir().getPath() + context.getPackageName() + "/shared_prefs"));
////    }
////
////    /**
////     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
////     */
////    private void deleteFilesByDirectory(File directory) {
////        LogT.i("directory:"+directory);
////        if (directory != null && directory.exists() && directory.isDirectory()) {
////            for (File item : directory.listFiles()) {
////                item.delete();
////            }
////        }
////    }
//
//
//    public String getCityListString() {
//        if(mSharedPreferences == null){
//            throw new NullPointerException("你还没有调用init(Context context)方法进行初始化");
//        }
//        return mSharedPreferences.getString("cityListString",null);
//    }
//
//    public void setCityListString(String cityListString) {
//        if(mSharedPreferences == null){
//            throw new NullPointerException("你还没有调用init(Context context)方法进行初始化");
//        }
//        mSharedPreferences.edit().putString("cityListString", cityListString).apply();
//    }
//
//    public String getRemindfulCity() {
//        return mSharedPreferences.getString("remindfulCity", null);
//    }
//
//    public void setRemindfulCity(String remindfulCity) {
//        mSharedPreferences.edit().putString("remindfulCity", remindfulCity).apply();
//    }
//    public String getRecentCallRecording() {
//        return mSharedPreferences.getString("recentCallRecording", "");
//    }
//
//    public void setRecentCallRecording(String recentCallRecording) {
//        mSharedPreferences.edit().putString("recentCallRecording", recentCallRecording).apply();
//    }
//
//    public boolean isRegistered() {
//        if(mSharedPreferences == null){
//            throw new NullPointerException("你还没有调用init(Context context)方法进行初始化");
//        }
//        return mSharedPreferences.getBoolean("registered", false);
//    }
//
//    public void setRegistered(boolean registered) {
//        mSharedPreferences.edit().putBoolean("registered", registered).apply();
//    }
//
//    public String getMobile() {
//        return mSharedPreferences.getString("mobile", null);
//    }
//
//    public void setMobile(String mobile) {
//        mSharedPreferences.edit().putString("mobile", mobile).apply();
//    }
//
//    /**想家宝的ID*/
//    public String getXJBUid() {
//        return mSharedPreferences.getString("xjbUid", null);
//    }
//
//    public void setXJBUid(String xjbUid) {
//        mSharedPreferences.edit().putString("xjbUid", xjbUid).apply();
//    }
//
//    /**想家宝的token，也就是topicTome*/
//    public String getXJBToken() {
//        return mSharedPreferences.getString("xjbToken", "");
//    }
//
//    public void setXJBToken(String xjbToken) {
//        mSharedPreferences.edit().putString("xjbToken", xjbToken).apply();
//    }
//
//    /**慈硕养老有2个id，一个是视频通话想家宝的XJBUid，还有一个是我们自己服务器userId*/
//    public String getUserId() {
//        return mSharedPreferences.getString("userId", "");
//    }
//
//    public void setUserId(String userId) {
//        mSharedPreferences.edit().putString("userId", userId).apply();
//    }
//    /**慈硕养老有2个token，一个是视频通话想家宝的xjbToken，还有一个是我们自己服务器CiShooToken*/
//
//    public String getCiShooToken() {
//        return mSharedPreferences.getString("CiShooToken", "");
//    }
//
//    public void setCiShooToken(String CiShooToken) {
//        mSharedPreferences.edit().putString("CiShooToken", CiShooToken).apply();
//    }
//    /**我的头像地址*/
//    public String getMyHeadPortraitPath() {
//        return mSharedPreferences.getString("myHeadPortraitPath", null);
//    }
//
//    public void setMyHeadPortraitPath(String myHeadPortraitPath) {
//        mSharedPreferences.edit().putString("myHeadPortraitPath", myHeadPortraitPath).apply();
//    }
//
//    /**用药提醒闹钟的时间组*/
//    public String getMedicationAlarmClockTimeArray(int id) {
//        return mSharedPreferences.getString(id+"medicationAlarmClockTimeArray", null);
//    }
//    public void setMedicationAlarmClockTimeArray(int id,String medicationAlarmClockTimeArray ){
//        mSharedPreferences.edit().putString(id+"medicationAlarmClockTimeArray", medicationAlarmClockTimeArray).apply();
//    }
//    /**用药提醒闹钟的开始时间日期*/
//    public long getMedicationAlarmClockStartDate(int id) {
//        return mSharedPreferences.getLong(id+"medicationAlarmClockStartDate",0);
//    }
//    public void setMedicationAlarmClockStartDate(int id,long medicationAlarmClockStartDate ){
//        mSharedPreferences.edit().putLong(id+"medicationAlarmClockStartDate", medicationAlarmClockStartDate).apply();
//    }
//    /**用药提醒闹钟的结束时间日期*/
//    public long getMedicationAlarmClockEndDate(int id) {
//        return mSharedPreferences.getLong(id+"medicationAlarmClockEndDate", 0);
//    }
//    public void setMedicationAlarmClockEndDate(int id,long medicationAlarmClockEndDate ){
//        mSharedPreferences.edit().putLong(id+"medicationAlarmClockEndDate", medicationAlarmClockEndDate).apply();
//    }
//    /**用药提醒闹钟的药品名称*/
//    public String getMedicationAlarmClockDrugName(int id) {
//        return mSharedPreferences.getString(id+"medicationAlarmClockDrugName", null);
//    }
//    public void setMedicationAlarmClockDrugName(int id,String medicationAlarmClockDrugName ){
//        mSharedPreferences.edit().putString(id+"medicationAlarmClockDrugName", medicationAlarmClockDrugName).apply();
//    }
//    /**用药提醒闹钟的药品剂量*/
//    public String getMedicationAlarmClockDose(int id) {
//        return mSharedPreferences.getString(id+"medicationAlarmClockDose", null);
//    }
//    public void setMedicationAlarmClockDose(int id,String medicationAlarmClockDose ){
//        mSharedPreferences.edit().putString(id+"medicationAlarmClockDose", medicationAlarmClockDose).apply();
//    }
//    /**用药提醒闹钟的药品剂量*/
//    public int getMedicationAlarmClockDoseUnit(int id) {
//        return mSharedPreferences.getInt(id+"medicationAlarmClockDoseUnit", 0);
//    }
//    public void setMedicationAlarmClockDoseUnit(int id,int medicationAlarmClockDoseUnit ){
//        mSharedPreferences.edit().putInt(id+"medicationAlarmClockDoseUnit", medicationAlarmClockDoseUnit).apply();
//    }
//    /**用药提醒闹钟的药品备注*/
//    public String getMedicationAlarmClockRemark(int id) {
//        return mSharedPreferences.getString(id+"medicationAlarmClockRemark", null);
//    }
//    public void setMedicationAlarmClockRemark(int id,String medicationAlarmClockRemark ){
//        mSharedPreferences.edit().putString(id+"medicationAlarmClockRemark", medicationAlarmClockRemark).apply();
//    }
//
//    /**呼吸警告范围*/
//    public String getBreathAlarmRange() {
//        return mSharedPreferences.getString("breathAlarmRange", null);
//    }
//
//    public void setBreathAlarmRange(String breathAlarmRange) {
//        mSharedPreferences.edit().putString("breathAlarmRange", breathAlarmRange).apply();
//    }
//
//    /************************/
//    /*免打扰模式*/
//    public boolean getWakeUpSwitch() {
//        return mSharedPreferences.getBoolean("wakeUpSwitch", false);
//    }
//
//    public void setWakeUpSwitch(boolean wakeUpSwitch) {
//        mSharedPreferences.edit().putBoolean("wakeUpSwitch", wakeUpSwitch).apply();
//    }
//
//    /*****peopleId*****/
//    public int getPeopleId() {
//        return mSharedPreferences.getInt("peopleId", 0);
//    }
//
//    public void setPeopleId(int peopleId) {
//        mSharedPreferences.edit().putInt("peopleId", peopleId).apply();
//    }
//    /*免打扰模式的时间*/
//    public String getDoNotDisturbTime(){
//        return mSharedPreferences.getString("disturbTime","");
//    }
//    public void setDoNotDisturbTime(String disturbTime) {
//        mSharedPreferences.edit().putString("disturbTime",disturbTime).apply();
//    }
//    public int getDoNotDisturbTimeStartHour(){
//        return mSharedPreferences.getInt("startHour",0);
//    }
//    public void setDoNotDisturbTimeStartHour(int disturbTime) {
//        mSharedPreferences.edit().putInt("startHour",disturbTime).apply();
//    }
//
//    public int getDoNotDisturbTimeStartMinute(){
//        return mSharedPreferences.getInt("startMinute",0);
//    }
//    public void setDoNotDisturbTimeStartMinute(int disturbTime) {
//        mSharedPreferences.edit().putInt("startMinute",disturbTime).apply();
//    }
//
//    public int getDoNotDisturbTimeEndHour(){
//        return mSharedPreferences.getInt("endHour",0);
//    }
//    public void setDoNotDisturbTimeEndHour(int disturbTime) {
//        mSharedPreferences.edit().putInt("endHour",disturbTime).apply();
//    }
//    public int getDoNotDisturbTimeEndMinute(){
//        return mSharedPreferences.getInt("endMinute",0);
//    }
//    public void setDoNotDisturbTimeEndMinute(int disturbTime) {
//        mSharedPreferences.edit().putInt("endMinute",disturbTime).apply();
//    }
//
//
//}
