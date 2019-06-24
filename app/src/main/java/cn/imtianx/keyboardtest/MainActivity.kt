package cn.imtianx.keyboardtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.imtianx.keyboard.KeyboardManager
import cn.imtianx.keyboard.NAVoiceKeyboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var keyboardManager: KeyboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyboardManager = KeyboardManager(this)

        keyboardManager.bindToEditor(et_custom_num_abc, NAVoiceKeyboard(this))

        btn_close.setOnClickListener {
            keyboardManager.hideSoftKeyboard()

        }
        btn_finish.setOnClickListener {
            System.exit(0)
        }
    }

}
