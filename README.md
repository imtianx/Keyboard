# Keyboard
> 自定义键盘，大写字母和数字，支持按键发音，键盘显示文本文本长度。

## 实现方式

使用原生 `keyboard` 定义键盘，然后自定义键盘处理按键事件，最后将其放入自定义 view 中 来显示使用。

## 主要问题
 
 1. 返回关闭键盘
  
  ```
  // 这里通过 `OnFocusChangeListener`监听，来显示/隐藏键盘，只需要失去焦点即可。
  override fun onBackPressed() {
        if (et_custom_num_abc.hasFocus()) {
            et_custom_num_abc.clearFocus()
        } else {
            super.onBackPressed()
        }
  }
  ```
 
 2. **避免系统键盘显示**
 
 ```
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
 ```


## 参考 
 - [djkeyboard](https://github.com/xudjx/djkeyboard): **大量使用反射**，`Android P` 之后存在问题。
 - [CustomizeKeyboard](https://github.com/StomHong/CustomizeKeyboard)：存在较多问题，如无法隐藏系统键盘等