//package com.linqinen.library.activity
//
//import android.app.AlertDialog
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
//import android.view.View
//import android.widget.Toast
//
///**
// * Created by 林 on 2018/3/15.
// */
//abstract class BaseActivityIan: AppCompatActivity() {
//
//    private var mToast: Toast? = null
//
//    /**用来显示Toast信息*/
//    protected fun showToast(content: String) {
//        showToast(content, Toast.LENGTH_SHORT)
//    }
//
//    protected fun showToast(content: String, duration: Int) {
//        if (mToast == null) {
//            mToast = Toast.makeText(applicationContext, content, duration)
//            mToast?.show()
//        } else {
//            mToast?.setText(content)
//            mToast?.show()
//        }
//    }
//
//    /**
//     * 检查是否拥有指定的所有权限
//     */
//     fun checkPermissionAllGranted(permissions: Array<String>): Boolean {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                // 只要有一个权限没有被授予, 则直接返回 false
//                return false
//            }
//        }
//        return true
//    }
//
//    /**
//     * 检查权限结果是否拥有指定的所有权限
//     */
//    fun checkGrantResultsAllGranted(grantResults: IntArray): Boolean {
//        for (grantResult in grantResults) {
//            if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                // 只要有一个权限没有被授予, 则直接返回 false
//                return false
//            }
//        }
//        return true
//    }
//
//    fun showDialog(){
//        AlertDialog.Builder(this)
//                .setMessage("未获得权限，无法使用功能")
//                .setPositiveButton("设置") { _, _ ->
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    intent.data = Uri.parse("package:$packageName")
//                    startActivity(intent)
//                }.setNegativeButton("取消", null)
//                .show()
//    }
//
//    protected fun startActivity(clazz: Class<*>, bundle: Bundle?) {
//        val intent = Intent(this, clazz)
//        if (bundle != null) {
//            intent.putExtras(bundle)
//        }
//        startActivity(intent)
//    }
//    protected fun startActivity(clazz: Class<*>) {
//        startActivity(clazz,null)
//    }
//
//    protected fun setViewVisible(view: View){
//        view.visibility = View.VISIBLE
//    }
//    protected fun setViewInvisible(view: View){
//        view.visibility = View.INVISIBLE
//    }
//    protected fun setViewGone(view: View){
//        view.visibility = View.GONE
//    }
//
//}