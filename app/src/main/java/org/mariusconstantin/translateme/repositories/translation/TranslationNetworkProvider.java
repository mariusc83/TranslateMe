package org.mariusconstantin.translateme.repositories.translation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.mariusconstantin.translateme.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MConstantin on 7/11/2016.
 */
public class TranslationNetworkProvider {
    private final Gson mGsonParser;
    private static final String API_KEY = BuildConfig.MY_TRANSLATE_API_KEY;
    private static final String API_URL =
            "https://www.googleapis.com/language/translate/v2?key=%s&source=%s&target=%s&q=%s";

    public TranslationNetworkProvider() {
        mGsonParser = new GsonBuilder().create();
    }

    /**
     * @param value
     * @return the translated model
     * @throws JsonSyntaxException
     */
    @Nullable
    public TranslationModel translate(@NonNull String value,
                                      @NonNull String fromLang,
                                      @NonNull String toLang) throws JsonSyntaxException {
        // new change
        /*final String data = fetchJSONFromUrl(String.format(Locale.ENGLISH,
                API_URL,
                API_KEY,
                fromLang,
                toLang,
                value));*/
        final String from = "{ \"data\": {  \"translations\": [   {    \"translatedText\": \"Hallo "
                + "Welt\"   }  ] }}";
        return new TranslationModel(mGsonParser.fromJson(from, TranslationDataModel.class));
        //return mGsonParser.fromJson(data, TranslationModel.class);
    }

    /**
     * Download a JSON file from a server, parse the content and return the JSON
     * object.
     *
     * @return result JSONObject containing the parsed representation.
     */
    private String fetchJSONFromUrl(String urlString) {
        // test the interactive rebase
        BufferedReader reader = null;
        try {
            HttpURLConnection urlConnection =
                    (HttpURLConnection) new URL(urlString).openConnection();
            final int status = urlConnection.getResponseCode();
            final InputStream inputStream =
                    status > 300 ? urlConnection.getErrorStream() : urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
