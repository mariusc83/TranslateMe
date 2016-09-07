package org.mariusconstantin.translateme.repositories.translation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MConstantin on 7/26/2016.
 */
public class TranslationModel {

    @SerializedName("data")
    private TranslationDataModel mData;

    public TranslationModel(TranslationDataModel data) {
        mData = data;
    }

    public TranslationDataModel getData() {
        return mData;
    }
}
