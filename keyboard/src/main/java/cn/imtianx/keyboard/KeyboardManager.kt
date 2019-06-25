package cn.imtianx.keyboard

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.*
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
class KeyboardManager constructor(private val context: Context) {

    private lateinit var mRootView: ViewGroup
    private lateinit var xKeyboardView: XKeyboardView
    private lateinit var mKeyboardContainerLayoutParams: FrameLayout.LayoutParams
    private lateinit var naVoiceKeyboard: NAVoiceKeyboard


    private val editorFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (v is EditText) {
            if (hasFocus) {
                v.postDelayed({
                    showSoftKeyboard(v)
                }, 300)
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
        editText.inputType = InputType.TYPE_NULL
        editText.setTag(R.id.bind_keyboard_2_editor, keyboard)
        editText.onFocusChangeListener = editorFocusChangeListener
        naVoiceKeyboard = keyboard
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


    /**
     * 显示键盘
     */
    fun showSoftKeyboard(editText: EditText) {
        (context as Activity).window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        hideSystemKeyboard()
        val keyboard = getBindKeyboard(editText)
        if (keyboard == null) {
            Log.e(TAG, "edit text not bind to keyboard")
            return
        }

        editText.isSelected = true
        editText.isCursorVisible = true
        editText.setTextIsSelectable(true)
        editText.setSelection(0)
        editText.requestFocus()
        keyboard.editText = editText
        keyboard.nextFocusView = xKeyboardView.editText
        initKeyboard(keyboard)
        if (mRootView.indexOfChild(xKeyboardView) == -1) {
            mRootView.addView(xKeyboardView, mKeyboardContainerLayoutParams)
        }
        xKeyboardView.visibility = View.VISIBLE
        xKeyboardView.animation =
            AnimationUtils.loadAnimation(context, R.anim.keyboard_down_to_up)
    }

    /**
     * 关闭键盘动画
     */
    fun hideSoftKeyboard() {
        hideSystemKeyboard()
        naVoiceKeyboard.hideKeyboard()
        hideKeyboardAnim()
    }

    private fun hideKeyboardAnim() {
        xKeyboardView.visibility = View.GONE
        xKeyboardView.animation =
            AnimationUtils.loadAnimation(context, R.anim.keyboard_up_to_hide)
    }

    /**
     * 隐藏系统键盘
     */
    fun hideSystemKeyboard() {
        if (context is Activity) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                context.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}