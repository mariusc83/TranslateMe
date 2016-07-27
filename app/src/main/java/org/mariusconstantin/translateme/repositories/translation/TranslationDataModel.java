package org.mariusconstantin.translateme.repositories.translation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MConstantin on 7/26/2016.
 * { "data": {  "translations": [   {    "translatedText": "Hallo Welt"   }  ] }}
 */
public class TranslationDataModel {

    @SerializedName("translations")
    private List<TranslatedTextModel> mTranslations;

    public List<TranslatedTextModel> getTranslations() {
        return mTranslations;
    }
}
