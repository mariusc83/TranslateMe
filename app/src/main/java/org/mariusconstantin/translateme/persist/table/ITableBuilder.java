package org.mariusconstantin.translateme.persist.table;

import android.support.annotation.NonNull;

import com.annimon.stream.function.Consumer;

/**
 * Created by MConstantin on 3/6/2016.
 */
public interface ITableBuilder {

    void executeBuildQuery(@NonNull Consumer<String> consumer);

    void executeRemoveQuery(@NonNull Consumer<String> consumer);

    void executeClearQuery(@NonNull Consumer<String> consumer);

}