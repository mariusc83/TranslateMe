package org.mariusconstantin.translateme.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.persist.table.ITableBuilder;
import org.mariusconstantin.translateme.utils.ILogger;

/**
 * Created by MConstantin on 8/23/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "translate_persist";
    private static final int DB_VERSION = 1;
    private final ILogger mLogger;
    private final ITableBuilder mTableBuilder;

    public DatabaseHelper(@AppContext Context context,
                          @NonNull ILogger logger,
                          @NonNull ITableBuilder tableBuilder) {
        super(context, DB_NAME, null, DB_VERSION);
        mLogger = logger;
        mTableBuilder = tableBuilder;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mTableBuilder.executeBuildQuery(db::execSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
