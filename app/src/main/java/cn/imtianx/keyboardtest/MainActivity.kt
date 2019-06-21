package cn.imtianx.keyboardtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.imtianx.keyboard.KeyboardManager
import cn.imtianx.keyboard.NAVoiceKeyboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KeyboardManager.getInstance(this).bindToEditor(et_custom_num_abc, NAVoiceKeyboard(this))

    }


    fun openKeyboard(view: View) {

    }
}
