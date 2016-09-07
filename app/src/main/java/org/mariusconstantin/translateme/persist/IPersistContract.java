package org.mariusconstantin.translateme.persist;

import android.net.Uri;

/**
 * Created by MConstantin on 8/23/2016.
 */
public interface IPersistContract {

    interface Tables {
        String TRANSLATIONS_TABLE = "translations";
    }

    String AUTHORITY = "org.mariusconstantin.translateme";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
