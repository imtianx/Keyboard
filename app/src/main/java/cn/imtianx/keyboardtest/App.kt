package cn.imtianx.keyboardtest

import android.app.Application

/**
 * <pre>
 *     @desc: app
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 17:32
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: Application
    }

}