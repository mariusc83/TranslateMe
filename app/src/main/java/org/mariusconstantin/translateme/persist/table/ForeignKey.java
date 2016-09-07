package org.mariusconstantin.translateme.persist.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by MConstantin on 9/7/2016.
 */
public class ForeignKey implements IForeignKey {


    @NonNull
    private final String mColumnName;
    @NonNull
    private final String mTargetTable;
    @Nullable
    private final List<Pair<Event, Action>> mEvents;

    @SuppressWarnings("NullArgumentToVariableArgMethod")
    public ForeignKey(@NonNull String columnName,
                      @NonNull String targetTable) {
        this(columnName, targetTable, null);
    }

    @SafeVarargs
    public ForeignKey(@NonNull String columnName,
                      @NonNull String targetTable,
                      @Nullable Pair<Event, Action>... events) {
        mColumnName = columnName;
        mTargetTable = targetTable;
        mEvents = events != null ? ImmutableList.copyOf(events) : null;
    }

    @NonNull
    @Override
    public String getTargetTable() {
        return mTargetTable;
    }

    @NonNull
    @Override
    public String getTargetColumnName() {
        return mColumnName;
    }

    @Nullable
    @Override
    public List<Pair<Event, Action>> getEvents() {
        return mEvents;
    }
}
