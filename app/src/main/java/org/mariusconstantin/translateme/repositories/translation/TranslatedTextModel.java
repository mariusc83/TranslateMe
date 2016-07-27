package org.mariusconstantin.translateme.repositories.translation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MConstantin on 7/26/2016.
 */
public class TranslatedTextModel {
    @SerializedName("translatedText")
    private String mTranslatedText;

    public String translatedText() {
        return mTranslatedText;
    }
}
