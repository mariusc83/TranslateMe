package org.mariusconstantin.translateme.repositories.translation;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by MConstantin on 7/11/2016.
 */
public class TranslationNetworkProvider {
    public String translate(@NonNull String value) {

        try {
            final JSONObject object = fetchJSONFromUrl("https://www.googleapis.com/language/translate/v2?key=AIzaSyBr0qb1-F7Y_bHDZ-UNTmVRCpBXTQSuF9E&source=en&target=de&q=Hello%20world");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Download a JSON file from a server, parse the content and return the JSON
     * object.
     *
     * @return result JSONObject containing the parsed representation.
     */
    private JSONObject fetchJSONFromUrl(String urlString) throws JSONException {
        BufferedReader reader = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
            final int status = urlConnection.getResponseCode();
            final InputStream inputStream = status > 300 ? urlConnection.getErrorStream() : urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
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
