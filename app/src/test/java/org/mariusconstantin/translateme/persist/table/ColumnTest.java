package org.mariusconstantin.translateme.persist.table;


import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MConstantin on 9/7/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 19, constants = BuildConfig.class)
public class ColumnTest {


    @Test
    public void testDefaultColumn() throws Exception {
        // given
        final String columnName = "test";
        final String columnType = IColumn.Type.INTEGER;

        // when
        final IColumn column = new Column.Builder(columnName, columnType).build();

        // then
        assertThat(column.getName()).isEqualTo(columnName);
        assertThat(column.getType()).isEqualTo(columnType);
    }

    @Test
    public void testPrimaryColumn() throws Exception {
        // given
        final String columnName = "test";
        final String columnType = IColumn.Type.INTEGER;

        // when
        final IColumn column = new Column.Builder(columnName, columnType)
                .isPrimary()
                .hasAutoIncrement()
                .build();

        // then
        assertThat(column.getName()).isEqualTo(columnName);
        assertThat(column.getType()).isEqualTo(columnType);
        assertThat(column.isPrimary()).isTrue();
        assertThat(column.hasAutoIncrement()).isTrue();
        assertThat(column.getForeignKey()).isNull();
    }

    @Test
    public void testColumnWithForeignKey() throws Exception {
        // given
        final String columnName = "test";
        final String columnType = IColumn.Type.INTEGER;
        final Pair<IForeignKey.Event, IForeignKey.Action> foreignKeyEvent = new
                Pair<>(IForeignKey.Event.ON_DELETE, IForeignKey.Action.CASCADE);
        final IForeignKey foreignKey = new ForeignKey(columnName, columnType, foreignKeyEvent);

        // when
        final IColumn column = new Column.Builder(columnName, columnType)
                .withForeignKeys(foreignKey)
                .build();

        // then
        assertThat(column.getName()).isEqualTo(columnName);
        assertThat(column.getType()).isEqualTo(columnType);
        assertThat(column.getForeignKey()).isEqualTo(foreignKey);
        assertThat(column.getForeignKey().getEvents().get(0)).isEqualTo(foreignKeyEvent);
    }
}