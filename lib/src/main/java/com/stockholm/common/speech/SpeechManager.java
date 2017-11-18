package com.stockholm.common.speech;


import android.content.Context;
import android.content.Intent;

import com.stockholm.common.utils.StockholmLogger;

public final class SpeechManager {

    private SpeechManager() {

    }

    public static void speakMessage(Context context, String message) {
        speakMessage(context, message, -1);
    }

    public static void speakMessage(Context context, String message, long speakId) {
        StockholmLogger.i("TTSService", "send speakId: " + speakId);
        Intent intent = new Intent(SpeechAction.ACTION_TTS_SPEAK);
        intent.putExtra(SpeechAction.KEY_TTS_SPEAK_MESSAGE, message);
        intent.putExtra(SpeechAction.KEY_TTS_SPEAK_ID, speakId);
        context.sendBroadcast(intent);
    }

}