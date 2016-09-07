package org.mariusconstantin.translateme.persist.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by MConstantin on 9/7/2016.
 */
public class Column implements IColumn {

    @ColumnType
    @NonNull
    private final String mType;
    @NonNull
    private final String mName;
    @Nullable
    private IForeignKey mForeignKey;

    private final boolean mIsPrimary;
    private final boolean mHasAutoIncrement;

    public Column(@NonNull Builder builder) {
        mType = builder.mType;
        mName = builder.mName;
        mForeignKey = builder.mForeignKey;
        mIsPrimary = builder.mIsPrimary;
        mHasAutoIncrement = builder.mHasAutoIncrement;
    }

    @ColumnType
    @NonNull
    @Override
    public String getType() {
        return mType;
    }

    @Override
    public boolean isPrimary() {
        return mIsPrimary;
    }

    @Override
    public boolean hasAutoIncrement() {
        return mHasAutoIncrement;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Nullable
    @Override
    public IForeignKey getForeignKey() {
        return mForeignKey;
    }

    public static class Builder {

        @ColumnType
        private final String mType;
        private final String mName;
        private IForeignKey mForeignKey;

        private boolean mIsPrimary = false;
        private boolean mHasAutoIncrement = false;

        public Builder(@NonNull String name,
                       @NonNull @ColumnType String type) {
            mType = type;
            mName = name;
        }


        public Builder isPrimary() {
            mIsPrimary = true;
            return this;
        }

        public Builder hasAutoIncrement() {
            mHasAutoIncrement = true;
            return this;
        }

        public Builder withForeignKeys(IForeignKey foreignKey) {
            mForeignKey = foreignKey;
            return this;
        }

        @NonNull
        public IColumn build() {
            return new Column(this);
        }
    }
}
