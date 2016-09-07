package org.mariusconstantin.translateme.persist.table;

import android.database.SQLException;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.annimon.stream.function.Consumer;
import java.util.List;

/**
 * Created by MConstantin on 3/6/2016.
 */
public class TableBuilder implements ITableBuilder {
    @NonNull
    private final String mTableName;
    @NonNull
    private final IColumn[] mColumns;


    private TableBuilder(@NonNull IColumn[] columns,
                         @NonNull String tableName) {
        mColumns = columns;
        mTableName = tableName;
    }

    /**
     * @param columns
     * @param tableName
     * @return the {@link ITableBuilder} instance
     * @throws IllegalArgumentException if the {@code columns} argument is empty
     */
    public static ITableBuilder createWith(@NonNull IColumn[] columns, @NonNull String tableName) {
        if (columns.length <= 0)
            throw new IllegalArgumentException("You cannot use a TableBuilder "
                    + "with no columns");
        return new TableBuilder(columns, tableName);
    }

    @Override
    public void executeBuildQuery(@NonNull Consumer<String> consumer) throws SQLException {
        final StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + mTableName);
        sb.append("(");
        for (IColumn column : mColumns) {
            sb.append("`")
                    .append(column.getName())
                    .append("`").append(" ")
                    .append(column.getType());
            if (column.isPrimary()) {
                sb.append(" PRIMARY KEY");
            }

            if (column.hasAutoIncrement()) {
                sb.append(" AUTOINCREMENT");
            }
            sb.append(", ");
        }
        for (IColumn column : mColumns) {
            final IForeignKey foreignKey = column.getForeignKey();
            if (foreignKey != null) {
                sb.append("FOREIGN KEY ")
                        .append("(`")
                        .append(column.getName())
                        .append("`)")
                        .append(" REFERENCES ")
                        .append(foreignKey.getTargetTable())
                        .append("(`")
                        .append(foreignKey.getTargetColumnName())
                        .append("`)");

                final List<Pair<IForeignKey.Event, IForeignKey.Action>> events = foreignKey
                        .getEvents();
                if (events != null && events.size() > 0) {
                    for (Pair<IForeignKey.Event, IForeignKey.Action> event : events) {
                        sb.append(" ")
                                .append(event.first.getName())
                                .append(" ")
                                .append(event.second.getName())
                                .append(",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                }

                sb.append(", ");
            }
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(");");
        consumer.accept(sb.toString());
    }

    @Override
    public void executeRemoveQuery(@NonNull Consumer<String> consumer) {
        consumer.accept("DROP TABLE IF EXISTS " + mTableName);
    }

    @Override
    public void executeClearQuery(@NonNull Consumer<String> consumer) {
        consumer.accept("DELETE FROM " + mTableName);
    }

}
