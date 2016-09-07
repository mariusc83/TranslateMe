package org.mariusconstantin.translateme.persist.inject;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.persist.DatabaseHelper;
import org.mariusconstantin.translateme.persist.table.ITableBuilder;
import org.mariusconstantin.translateme.utils.ILogger;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 9/5/2016.
 */
@Module
public class MockPersistModule {


    private final ITableBuilder tableBuilder;

    public MockPersistModule(ITableBuilder tableBuilder) {
        this.tableBuilder = tableBuilder;
    }

    @NonNull
    @PerPersist
    @Provides
    public DatabaseHelper provideDatabaseHelper(@NonNull @AppContext Context context,
                                                @NonNull ILogger logger) {
        return new DatabaseHelper(context, logger, tableBuilder);
    }
}
