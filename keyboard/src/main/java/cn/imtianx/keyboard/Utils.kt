package cn.imtianx.keyboard

import android.app.Application

/**
 * <pre>
 *     @desc:
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-07-05 13:09
 */
object Utils {

    private var application: Application? = null

    fun init(app: Application) {
        this.application = app
    }

    fun getApp(): Application {
        if (application != null) {
            return application!!
        } else {
            throw ExceptionInInitializerError("not init application,you should call init on application")
        }
    }
}