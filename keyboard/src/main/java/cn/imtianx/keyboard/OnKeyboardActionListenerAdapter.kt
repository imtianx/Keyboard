package cn.imtianx.keyboard

import android.inputmethodservice.KeyboardView

/**
 * <pre>
 *     @desc:
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 09:39
 */
interface OnKeyboardActionListenerAdapter : KeyboardView.OnKeyboardActionListener {

    override fun swipeRight() {
    }

    override fun onPress(primaryCode: Int) {
    }

    override fun onRelease(primaryCode: Int) {
    }

    override fun swipeLeft() {
    }

    override fun swipeUp() {
    }

    override fun swipeDown() {
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray) {
    }

    override fun onText(text: CharSequence) {
    }
}