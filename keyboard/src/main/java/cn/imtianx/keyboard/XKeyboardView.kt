package cn.imtianx.keyboard

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout

/**
 * <pre>
 *     @desc:
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-06-21 14:14
 */
class XKeyboardView : LinearLayout {

    var editText: EditText
    var naVoiceKeyboardView: NAVoiceKeyboardView
    var llContainer: LinearLayout

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        val view =
            LayoutInflater.from(context).inflate(
                R.layout.layout_keyboard_view_num_abc,
                this, true
            )

        editText = view.findViewById(R.id.et_hide)
        naVoiceKeyboardView = view.findViewById(R.id.kv_voice)
        llContainer = view.findViewById(R.id.ll_container)
    }

}
