package cn.imtianx.keyboard

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

/**
 * <pre>
 *     @desc: speech utils
 * </pre>
 * @author imtianx
 * @email imtianx@gmail.com
 * @date 2019-07-05 09:30
 */
object SpeechUtil {


    private const val NOT_INIT = -1024
    private var langResult = NOT_INIT
    private lateinit var textToSpeech: TextToSpeech

    private val mediaPlayerCache = hashMapOf<String, MediaPlayer?>()

    /**
     * must be init in advance
     */
    fun init() {
        textToSpeech = TextToSpeech(Utils.getApp(), TextToSpeech.OnInitListener {
            langResult = textToSpeech.setLanguage(Locale.CHINA)
            textToSpeech.setPitch(1f)
            textToSpeech.setSpeechRate(1f)
        })
    }

    private fun isSupportTTS() =
        textToSpeech.isLanguageAvailable(Locale.CHINA) >= TextToSpeech.LANG_AVAILABLE

    /**
     * speak
     */
    fun speak(text: String, useMp3: Boolean = false) {
        if (isSupportTTS() && useMp3.not()) {
            Log.e("tx", "----------------speak by tts")
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        } else {
            if (!mediaPlayerCache.containsKey(text) || null == mediaPlayerCache[text]) {
                mediaPlayerCache[text] =
                    MediaPlayer.create(Utils.getApp(), getResId("key_$text"))
            }
            mediaPlayerCache[text]?.start()
            Log.e("tx", "----------------speek by mp3")
        }
    }

    /**
     * get id
     */
    private fun getResId(name: String, type: String = "raw") =
        Utils.getApp().resources.getIdentifier(
            name, type, Utils.getApp().packageName
        )
}