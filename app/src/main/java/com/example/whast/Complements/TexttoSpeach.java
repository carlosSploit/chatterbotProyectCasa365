package com.example.whast.Complements;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TexttoSpeach implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Context ContextxGene;
    private boolean band = false;

    public TexttoSpeach(Context c) {
        ContextxGene = c ;
        tts = new TextToSpeech(ContextxGene,this);

    }

    public boolean isBand() {
        return band;
    }

    public void setBand(boolean band) {
        this.band = band;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.getDefault());
            if(result== TextToSpeech.LANG_NOT_SUPPORTED || result== TextToSpeech.LANG_MISSING_DATA){
                Log.e("TextSpeach: ","Este Lenguage no es soportado.");
            }else{
                this.setBand(true);
            }
        }else{
            Log.e("TextSpeach","La Inizialisacion del lenguage es fallida.");
        }
    }

    public void speekOut(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }

    public TextToSpeech getTts() {
        return tts;
    }
}
