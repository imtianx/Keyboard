package cn.imtianx.keyboard

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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

    @JvmOverloads
    fun bindToEditor(
        editText: EditText,
        needAddTextChangeListener: Boolean = false,
        keyboard: NAVoiceKeyboard = NAVoiceKeyboard(context)
    ) {
        fixShowSystemKeyboard(editText)
        editText.setTag(R.id.bind_keyboard_2_editor, keyboard)
        editText.onFocusChangeListener = editorFocusChangeListener
        naVoiceKeyboard = keyboard
        // 通过监听更新键盘，避免因为 cut/paste 等直接修改 文本导致键盘无法更新
        if (needAddTextChangeListener) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateKeyboardViewText(s.toString())
                }

            })
        }
    }

    // update text from outer
    fun updateKeyboardViewText(text: String) {
        try {
            (xKeyboardView.naVoiceKeyboardView.keyboard as NAVoiceKeyboard)
                .updateKeyboardViewText(
                    true,
                    text
                )
        } catch (e: Exception) {
            naVoiceKeyboard.updateKeyboardViewText(true, text)
        }
    }

    /**
     * 解决弹出系统键盘
     */
    private fun fixShowSystemKeyboard(editText: EditText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.showSoftInputOnFocus = false
        } else {
            try {
                EditText::class.java.getMethod("setShowSoftInputOnFocus", Boolean::class.java)
                    .apply {
                        isAccessible = true
                        invoke(editText, false)
                    }
            } catch (e: Exception) {
                editText.inputType = InputType.TYPE_NULL // 不会显示输入光标
            }
        }
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