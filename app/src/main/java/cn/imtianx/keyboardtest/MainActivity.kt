package cn.imtianx.keyboardtest

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import cn.imtianx.keyboard.KeyboardManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var keyboardManager: KeyboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyboardManager = KeyboardManager(this)

        keyboardManager.bindToEditor(et_custom_num_abc)

        btn_close.setOnClickListener {
            et_custom_num_abc.clearFocus()
        }
        setVmPolicy()
    }

    private fun setVmPolicy() {
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .apply {
                    penaltyDeath()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        detectNonSdkApiUsage()
                    }
                    penaltyLog()
                }.build()
        )
    }

    override fun onBackPressed() {
        if (et_custom_num_abc.hasFocus()) {
            // 通过清除焦点关闭键盘
            et_custom_num_abc.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

}
