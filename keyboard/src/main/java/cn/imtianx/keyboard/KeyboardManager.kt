package cn.imtianx.keyboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout

/**
 * <pre>
 *     @desc: 键盘管理器
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 11:03
 */
class KeyboardManager constructor(val context: Context) {

    private lateinit var mRootView: ViewGroup
    private lateinit var xKeyboardView: XKeyboardView
    private lateinit var mKeyboardContainerLayoutParams: FrameLayout.LayoutParams


    private val editorFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (v is EditText) {
            if (hasFocus) {
                v.postDelayed({ showSoftKeyboard(v) }, 300)
            } else {
                hideSoftKeyboard()
            }
        }
    }

    init {
        if (context is Activity) {
            mRootView = context.window.decorView.findViewById(android.R.id.content)
            xKeyboardView = LayoutInflater.from(context).inflate(
                R.layout.layout_keyboard_view_x, null
            ) as XKeyboardView
            mKeyboardContainerLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT
            )
            mKeyboardContainerLayoutParams.gravity = Gravity.BOTTOM
        } else {
            Log.e(TAG, "context must be activity")
        }
    }

    fun bindToEditor(editText: EditText, keyboard: NAVoiceKeyboard) {
        editText.setTag(R.id.bind_keyboard_2_editor, keyboard)
        editText.onFocusChangeListener = editorFocusChangeListener
    }

    private fun getBindKeyboard(editText: EditText?): NAVoiceKeyboard? {
        return if (editText != null) {
            editText.getTag(R.id.bind_keyboard_2_editor) as NAVoiceKeyboard
        } else null
    }

    private fun initKeyboard(keyboard: NAVoiceKeyboard) {
        xKeyboardView.naVoiceKeyboardView.keyboard = keyboard
        xKeyboardView.naVoiceKeyboardView.isEnabled = true
        xKeyboardView.naVoiceKeyboardView.isPreviewEnabled = false
        xKeyboardView.naVoiceKeyboardView.setOnKeyboardActionListener(keyboard)
        keyboard.updateKeyListener = xKeyboardView.naVoiceKeyboardView
    }


    private fun showSoftKeyboard(editText: EditText) {
        hideSystemKeyboard()
        val keyboard = getBindKeyboard(editText)
        if (keyboard == null) {
            Log.e(TAG, "edit text not bind to keyboard")
            return
        }
        keyboard.editText = editText
        keyboard.nextFocusView = xKeyboardView.editText
        initKeyboard(keyboard)
        if (mRootView.indexOfChild(xKeyboardView) == -1) {
            mRootView.addView(xKeyboardView, mKeyboardContainerLayoutParams)
        } else {
            xKeyboardView.visibility = View.VISIBLE
        }
        xKeyboardView.animation =
            AnimationUtils.loadAnimation(context, R.anim.keyboard_down_to_up)
    }

    /**
     * 关闭键盘动画
     */
    private fun hideSoftKeyboard() {
        xKeyboardView.visibility = View.GONE
        xKeyboardView.animation =
            AnimationUtils.loadAnimation(context, R.anim.keyboard_up_to_hide)
    }

    /**
     * 隐藏系统键盘
     */
    private fun hideSystemKeyboard() {
        if (context is Activity) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                context.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: KeyboardManager? = null

        fun getInstance(context: Context): KeyboardManager {
            if (instance == null) {
                synchronized(KeyboardManager::class)
                {
                    if (instance == null) {
                        instance = KeyboardManager(context)
                    }
                }
            }
            return instance!!
        }
    }

}