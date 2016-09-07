package org.mariusconstantin.translateme.persist.inject;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.persist.DatabaseHelper;
import org.mariusconstantin.translateme.persist.IPersistContract;
import org.mariusconstantin.translateme.persist.table.IColumn;
import org.mariusconstantin.translateme.persist.table.ITableBuilder;
import org.mariusconstantin.translateme.persist.table.TableBuilder;
import org.mariusconstantin.translateme.utils.ILogger;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 8/23/2016.
 */
@Module
public class PersistModule {


    @NonNull
    @Provides
    public ITableBuilder provideTableBuilder() {
        return TableBuilder.createWith(new IColumn[0], IPersistContract.Tables.TRANSLATIONS_TABLE);
    }

    @NonNull
    @PerPersist
    @Provides
    public DatabaseHelper provideDatabaseHelper(@NonNull @AppContext Context context,
                                                @NonNull ILogger logger,
                                                @NonNull ITableBuilder tableBuilder) {
        return new DatabaseHelper(context, logger, tableBuilder);
    }
}
