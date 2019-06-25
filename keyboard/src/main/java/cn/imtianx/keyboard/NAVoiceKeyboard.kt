package cn.imtianx.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.speech.tts.TextToSpeech
import android.support.annotation.DrawableRes
import android.support.annotation.IntegerRes
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import java.lang.ref.WeakReference
import java.util.*

/**
 * <pre>
 *     @desc: 数字、字母大写、播报键盘
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 09:58
 */
class NAVoiceKeyboard @JvmOverloads constructor(
    val context: Context,
    xmlLayoutResId: Int = R.xml.keyboard_num_abc,
    modeId: Int = 0
) :
    Keyboard(context, xmlLayoutResId, modeId), OnKeyboardActionListenerAdapter {

    var editText: EditText? = null
    var nextFocusView: View? = null

    private var speakKeyIndex = DEFAULT_INVALID_CODE
    private var speakStatus = false
    private var showTextKeyIndex = DEFAULT_INVALID_CODE

    var updateKeyListener: UpdateKeyListener? = null

    private var textToSpeech: TextToSpeech? = null
    private var contextReference: WeakReference<Context> = WeakReference(context)

    init {
        contextReference.get()?.let {
            textToSpeech = TextToSpeech(it, TextToSpeech.OnInitListener {
                textToSpeech?.language = Locale.CHINA
                textToSpeech?.setPitch(1f)
                textToSpeech?.setSpeechRate(1f)
            })
        }
        isShifted = true
        speakKeyIndex = getKeyIndex(R.integer.key_code_speech)
        showTextKeyIndex = getKeyIndex(R.integer.key_code_show_text)
    }

    /**
     * 获取指定 key 的 index
     */
    private fun getKeyIndex(@IntegerRes targetKeyCodeRes: Int): Int {
        val targetKeyCode = getKeyCode(targetKeyCodeRes)
        keys.forEachIndexed { index, key ->
            if (key.codes[0] == targetKeyCode) {
                return index
            }
        }
        return DEFAULT_INVALID_CODE
    }

    /**
     * 获取 keyCode
     */
    private fun getKeyCode(@IntegerRes redId: Int): Int {
        return context.resources.getInteger(redId)
    }

    /**
     * 更新 key icon
     */
    private fun updateKeyIcon(keyIndex: Int, @DrawableRes drawableRes: Int) {
        keys[keyIndex].apply {
            icon = context.resources.getDrawable(drawableRes)
        }
    }

    /**
     * 更新 key label
     */
    private fun updateKeyLabel(keyIndex: Int, newLabel: String) {
        keys[keyIndex].apply {
            label = newLabel
        }
    }

    /**
     * 处理 键盘 key 回调
     */
    override fun onKey(primaryCode: Int, keyCodes: IntArray) {
        if (null != editText && editText!!.hasFocus()) {
            val editable = editText!!.text
            val start = editText!!.selectionStart
            val end = editText!!.selectionEnd
            if (end > start) {
                editable.delete(start, end)
            }

            when (primaryCode) {
                KEYCODE_DELETE -> {
                    // 删除,
                    if (!TextUtils.isEmpty(editable)) {
                        if (start > 0) {
                            editable.delete(start - 1, start)
                            updateKeyboardViewText()
                        }
                    }
                }
                getKeyCode(R.integer.key_code_show_text) -> {
                    // 显示文本
                    return
                }
                getKeyCode(R.integer.key_code_complete) -> {
                    // 完成
                    hideKeyboard()
                    return
                }
                getKeyCode(R.integer.key_code_speech) -> {
                    // 发音图标
                    speakStatus = speakStatus.not()
                    updateKeyboardViewIcon()
                }
                else -> {
                    val abc = adjustCase(Character.toString(primaryCode.toChar()))
                    editable.insert(start, abc)
                    if (speakStatus) {
                        textToSpeech?.speak(abc.toString(), TextToSpeech.QUEUE_FLUSH, null)
                    }
                    /*
                     通过 addTextChangedListener 监听来修改
                      */
                    // updateKeyboardViewText()
                }
            }
        }
    }

    /**
     * 更新 icon
     */
    private fun updateKeyboardViewIcon() {
        updateKeyIcon(
            speakKeyIndex, if (speakStatus) {
                R.drawable.ic_num_abc_voice_selected
            } else {
                R.drawable.ic_num_abc_voice_normal
            }
        )
        updateKeyListener?.updateKeyOnKeyboard(speakKeyIndex)
    }

    /**
     * 更新文本
     */
    fun updateKeyboardViewText(fromOuter: Boolean = false, newDataText: String = "") {
        if (fromOuter) {
            updateKeyLabel(showTextKeyIndex, "${newDataText.length} 位")
        } else {
            updateKeyLabel(showTextKeyIndex, "${editText?.text.toString().length} 位")
        }
        updateKeyListener?.updateKeyOnKeyboard(showTextKeyIndex)
    }

    /**
     * 调整大小写
     */
    private fun adjustCase(label: CharSequence): CharSequence {
        return if (isShifted && label.length < 3 && Character.isLowerCase(label[0])
        ) {
            label.toString().toUpperCase()
        } else {
            label
        }
    }

    fun hideKeyboard() = if (nextFocusView == null) {
        false
    } else {
        nextFocusView!!.requestFocus()
    }

    companion object {
        const val DEFAULT_INVALID_CODE = -10000
    }
}