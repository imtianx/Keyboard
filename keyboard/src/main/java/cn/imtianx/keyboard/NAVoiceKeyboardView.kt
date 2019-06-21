package cn.imtianx.keyboard

import android.annotation.TargetApi
import android.content.Context
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.util.AttributeSet

/**
 * <pre>
 *     @desc: 数字、字母大写、播报键盘控件
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 10:00
 */
class NAVoiceKeyboardView : KeyboardView, UpdateKeyListener {

    // @formatter:off
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    // @formatter:on

    override fun updateKeyOnKeyboard(keyIndex: Int) {
        if (keyIndex != NAVoiceKeyboard.DEFAULT_INVALID_CODE) {
            invalidateKey(keyIndex)
        }
    }
}