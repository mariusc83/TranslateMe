package org.mariusconstantin.translateme.persist.table;


import android.util.Pair;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MConstantin on 9/6/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 19, constants = BuildConfig.class)
public class TableBuilderTest {
    private static final String TABLE_NAME = "Sample";

    ITableBuilder mTableBuilder;


    @Test(expected = IllegalArgumentException.class)
    public void onInstantiateTableBuilderWithoutColumns_shouldThrowIllegalArgumentException() {
        // given

        // when
        mTableBuilder = TableBuilder.createWith(new IColumn[]{},
                TABLE_NAME);

        // then
    }

    @Test
    public void onSimpleTableCreation_shouldReturnCorrectQuery() throws Exception {
        // given
        final String idColumnName = "id";
        final String nameColumnName = "name";
        final String descColumnName = "desc";
        final String buildQuery = "CREATE TABLE IF NOT EXISTS Sample(`"
                + idColumnName
                + "` integer PRIMARY KEY AUTOINCREMENT, `"
                + nameColumnName
                + "` text, `"
                + descColumnName
                + "` text"
                + ");";

        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        final IColumn column3 = mock(IColumn.class);
        given(column1.getName()).willReturn(idColumnName);
        given(column2.getName()).willReturn(nameColumnName);
        given(column3.getName()).willReturn(descColumnName);
        given(column1.getType()).willReturn(IColumn.Type.INTEGER);
        given(column1.isPrimary()).willReturn(true);
        given(column1.hasAutoIncrement()).willReturn(true);
        given(column2.getType()).willReturn(IColumn.Type.TEXT);
        given(column3.getType()).willReturn(IColumn.Type.TEXT);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2, column3},
                TABLE_NAME);

        // when
        mTableBuilder.executeBuildQuery(query -> assertThat(query).isEqualTo(buildQuery));

        // then
    }

    @Test
    public void onTableRemove_shouldReturnCorrectQuery() throws Exception {

        // given
        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2},
                TABLE_NAME);

        // when
        mTableBuilder.executeRemoveQuery(query -> assertThat(query)
                .isEqualTo("DROP TABLE IF EXISTS"
                        + " "
                        + TABLE_NAME));
    }

    @Test
    public void onTableCleared_shouldReturnCorrectQuery() throws Exception {

        // given
        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2},
                TABLE_NAME);

        // when
        mTableBuilder.executeClearQuery(query -> assertThat(query).isEqualTo("DELETE FROM "
                + TABLE_NAME));
    }

    @Test
    public void onTableCreationWithForeignKey_shouldReturnCorrectQuery() throws Exception {
        // given
        final String foreignKeyColumnName = "id";
        final String foreignKeyReferenceTableName = "ref_table";
        final String foreignKeyReferenceColumnName = "ref_table_column";
        final String idColumnName = "id";
        final String nameColumnName = "name";
        final String descColumnName = "desc";
        final String buildQuery = "CREATE TABLE IF NOT EXISTS Sample(`"
                + idColumnName
                + "` integer PRIMARY KEY AUTOINCREMENT, `"
                + nameColumnName
                + "` text, `"
                + descColumnName
                + "` text,"
                + " FOREIGN KEY (`"
                + foreignKeyColumnName + "`) REFERENCES " + foreignKeyReferenceTableName
                + "(`"
                + foreignKeyReferenceColumnName + "`)"
                + ");";

        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        final IColumn column3 = mock(IColumn.class);
        final IForeignKey foreignKey = mock(IForeignKey.class);

        given(column1.getName()).willReturn(idColumnName);
        given(column2.getName()).willReturn(nameColumnName);
        given(column3.getName()).willReturn(descColumnName);
        given(column1.getType()).willReturn(IColumn.Type.INTEGER);
        given(column1.isPrimary()).willReturn(true);
        given(column1.hasAutoIncrement()).willReturn(true);
        given(column2.getType()).willReturn(IColumn.Type.TEXT);
        given(column3.getType()).willReturn(IColumn.Type.TEXT);
        given(foreignKey.getTargetColumnName()).willReturn(foreignKeyReferenceColumnName);
        given(foreignKey.getTargetTable()).willReturn(foreignKeyReferenceTableName);
        given(column1.getForeignKey()).willReturn(foreignKey);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2, column3},
                TABLE_NAME);

        // when
        mTableBuilder.executeBuildQuery(query -> assertThat(query).isEqualTo(buildQuery));

        // then

    }

    @Test
    public void onTableCreationWithForeignKeys_shouldReturnCorrectQuery() throws Exception {
        // given
        final String foreignKeyColumnName1 = "id";
        final String foreignKeyReferenceTableName1 = "ref_table1";
        final String foreignKeyReferenceColumnName1 = "ref_table_column1";
        final String foreignKeyColumnName2 = "name";
        final String foreignKeyReferenceTableName2 = "ref_table2";
        final String foreignKeyReferenceColumnName2 = "ref_table_column2";
        final String idColumnName = "id";
        final String nameColumnName = "name";
        final String descColumnName = "desc";
        final String buildQuery = "CREATE TABLE IF NOT EXISTS Sample(`"
                + idColumnName
                + "` integer PRIMARY KEY AUTOINCREMENT, `"
                + nameColumnName
                + "` text, `"
                + descColumnName
                + "` text,"
                + " FOREIGN KEY (`"
                + foreignKeyColumnName1 + "`) REFERENCES " + foreignKeyReferenceTableName1
                + "(`"
                + foreignKeyReferenceColumnName1 + "`)"
                + ", FOREIGN KEY (`"
                + foreignKeyColumnName2 + "`) REFERENCES " + foreignKeyReferenceTableName2
                + "(`"
                + foreignKeyReferenceColumnName2 + "`)"
                + ");";

        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        final IColumn column3 = mock(IColumn.class);
        final IForeignKey foreignKey = mock(IForeignKey.class);
        final IForeignKey foreignKey2 = mock(IForeignKey.class);

        given(column1.getName()).willReturn(idColumnName);
        given(column2.getName()).willReturn(nameColumnName);
        given(column3.getName()).willReturn(descColumnName);
        given(column1.getType()).willReturn(IColumn.Type.INTEGER);
        given(column1.isPrimary()).willReturn(true);
        given(column1.hasAutoIncrement()).willReturn(true);
        given(column2.getType()).willReturn(IColumn.Type.TEXT);
        given(column3.getType()).willReturn(IColumn.Type.TEXT);
        given(foreignKey.getTargetColumnName()).willReturn(foreignKeyReferenceColumnName1);
        given(foreignKey.getTargetTable()).willReturn(foreignKeyReferenceTableName1);
        given(foreignKey2.getTargetColumnName()).willReturn(foreignKeyReferenceColumnName2);
        given(foreignKey2.getTargetTable()).willReturn(foreignKeyReferenceTableName2);
        given(column1.getForeignKey()).willReturn(foreignKey);
        given(column2.getForeignKey()).willReturn(foreignKey2);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2, column3},
                TABLE_NAME);

        // when
        mTableBuilder.executeBuildQuery(query -> assertThat(query).isEqualTo(buildQuery));

        // then

    }

    @Test
    public void onTableCreationWithForeignKeyAndAction_shouldReturnCorrectQuery() throws Exception {
        // given
        final String foreignKeyColumnName = "id";
        final String foreignKeyReferenceTableName = "ref_table";
        final String foreignKeyReferenceColumnName = "ref_table_column";
        final String idColumnName = "id";
        final String nameColumnName = "name";
        final String descColumnName = "desc";
        final Pair<IForeignKey.Event, IForeignKey.Action> event1 = new Pair<>(IForeignKey.Event
                .ON_UPDATE, IForeignKey.Action.CASCADE);
        final Pair<IForeignKey.Event, IForeignKey.Action> event2 = new Pair<>(IForeignKey.Event
                .ON_DELETE, IForeignKey.Action.CASCADE);
        final IColumn column1 = mock(IColumn.class);
        final IColumn column2 = mock(IColumn.class);
        final IColumn column3 = mock(IColumn.class);
        final IForeignKey foreignKey = mock(IForeignKey.class);

        final String buildQuery = "CREATE TABLE IF NOT EXISTS Sample(`"
                + idColumnName
                + "` integer PRIMARY KEY AUTOINCREMENT, `"
                + nameColumnName
                + "` text, `"
                + descColumnName
                + "` text,"
                + " FOREIGN KEY (`"
                + foreignKeyColumnName + "`) REFERENCES " + foreignKeyReferenceTableName
                + "(`"
                + foreignKeyReferenceColumnName + "`)"
                + " " + event1.first.getName() + " " + event1.second.getName()
                + ", " + event2.first.getName() + " " + event2.second.getName()
                + ");";


        given(column1.getName()).willReturn(idColumnName);
        given(column2.getName()).willReturn(nameColumnName);
        given(column3.getName()).willReturn(descColumnName);
        given(column1.getType()).willReturn(IColumn.Type.INTEGER);
        given(column1.isPrimary()).willReturn(true);
        given(column1.hasAutoIncrement()).willReturn(true);
        given(column2.getType()).willReturn(IColumn.Type.TEXT);
        given(column3.getType()).willReturn(IColumn.Type.TEXT);
        given(foreignKey.getTargetColumnName()).willReturn(foreignKeyReferenceColumnName);
        given(foreignKey.getTargetTable()).willReturn(foreignKeyReferenceTableName);
        given(foreignKey.getEvents()).willReturn(ImmutableList.of(event1, event2));
        given(column1.getForeignKey()).willReturn(foreignKey);
        mTableBuilder = TableBuilder.createWith(new IColumn[]{column1, column2, column3},
                TABLE_NAME);

        // when
        mTableBuilder.executeBuildQuery(query -> assertThat(query).isEqualTo(buildQuery));

        // then

    }

}