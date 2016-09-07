package org.mariusconstantin.translateme.persist.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.util.List;

/**
 * Created by MConstantin on 9/6/2016.
 */
public interface IForeignKey {

    @NonNull
    String getTargetTable();

    @NonNull
    String getTargetColumnName();

    @Nullable
    List<Pair<Event, Action>> getEvents();


    enum Event {

        ON_UPDATE("ON UPDATE"),
        ON_DELETE("ON DELETE");

        private final String mName;

        Event(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

    }

    enum Action {

        SET_NULL("SET NULL"),
        SET_DEFAULT("SET DEFAULT"),
        RESTRICT("RESTRICT"),
        CASCADE("CASCADE"),
        NO_ACTION("NO ACTION");

        private final String mName;

        Action(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }
}
