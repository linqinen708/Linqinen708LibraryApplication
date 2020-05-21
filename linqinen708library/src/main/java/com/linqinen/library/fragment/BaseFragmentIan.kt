package com.linqinen.library.fragment

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast

/**
 * Created by Ian on 2019/11/15.
 */
abstract class BaseFragmentIan : Fragment() {

    companion object {

        private var mToast: Toast? = null
    }

    /**用来显示Toast信息*/
    protected fun showToast(content: String) {
        showToast(content, Toast.LENGTH_SHORT)
    }

    protected fun showToast(content: String, duration: Int) {
        if (mContext == null) {
            return
        }
        if (mToast == null) {
            mToast = Toast.makeText(mContext!!.applicationContext, content, duration)
            mToast?.show()
        } else {
            mToast?.setText(content)
            mToast?.show()
        }
    }

    /**使用onAttach方法获得context，可以有效解决“内存重启”
     *
     * 避免getActivity为null
     *
     * https://blog.csdn.net/qq_31010739/article/details/83348085
     */
    protected var mContext: Context? = null

    /**23版本之后才会触发 */
    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }

    /**
     * It's not called because this method has been added in API 23.
     * If you run your application on a device with API 23 (marshmallow) then onAttach(Context) will be called.
     * On all previous AndroidVersions onAttach(Activity) will be called.
     *
     * 链接：https://www.jianshu.com/p/91e9538631f9
     *
     * 在23版本之后，该方法被弃用，谷歌推荐使用onAttach(@NonNull Context context)
     */
    override fun onAttach(@NonNull activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }

    }

    private fun onAttachToContext(context: Context) {
        mContext = context
    }

    protected fun setViewVisible(@NonNull view: View) {
        view.visibility = View.VISIBLE
    }

    protected fun setViewInvisible(@NonNull view: View) {
        view.visibility = View.INVISIBLE
    }

    protected fun setViewGone(@NonNull view: View) {
        view.visibility = View.GONE
    }
}