package com.example.bradmoeller.myapplication.data;


import static com.example.bradmoeller.myapplication.data.Preconditions.checkState;

public class DatabaseColumn extends AbsVersionedObject {

    private String mColumnId;
    private boolean mNullable = true;
    private boolean mIsPrimaryKey = false;
    private boolean mAutoIncrement = false;
    private DataType mDataType = DataType.TEXT;

    private static final String NOT_NULL = "not null";
    private static final String PRIMARY_KEY = "primary key";
    private static final String AUTOINCREMENT = "autoincrement";

    public DatabaseColumn setVersion(int majorVersion, int minorVersion, int patchVersion, int buildVersion) {
        setObjectVersion(majorVersion, minorVersion, patchVersion, buildVersion);
        return this;
    }

    public enum DataType {
        INTEGER, TEXT
    }

    public DatabaseColumn(String id, DataType type) {
        checkState(id != null, "Cannot set id column to null");

        this.mColumnId = id;
        setDataType(type);
    }

    DatabaseColumn setDataType(DataType type) {
        checkState(type != null, "DataType cannot be null");

        if (!mDataType.equals(type)) {
            mDataType = type;
            if (!type.equals(DataType.INTEGER)) {
                isAutoIncrement(false);
            }
        }
        return this;
    }

    public DatabaseColumn isAutoIncrement(boolean autoIncrement) {
        checkState((autoIncrement && mDataType.equals((DataType.INTEGER))), "Cannot set autoincrement on a non-integer column");
        mAutoIncrement = autoIncrement;
        return this;
    }

    public String getColumnId() {
        return mColumnId;
    }

    public boolean isNullable() {
        return mNullable;
    }

    public DatabaseColumn isPrimaryKey(boolean isPrimaryKey) {
        mIsPrimaryKey = isPrimaryKey;
        if (mIsPrimaryKey) {
            mNullable = false;
        }
        return this;
    }

    public String getCreateClause() {
        String clause = String.format("%s %s %s", mColumnId, getDataTypeForClause(), getColumnDescriptors());
        return clause.trim();
    }

    private String getDataTypeForClause() {
        return mDataType.name();
    }

    private String getPrimaryKeyForClause() {
        return mIsPrimaryKey ? PRIMARY_KEY : "";
    }

    private String getColumnDescriptors() {
        String descriptors = getPrimaryKeyForClause();
        if (mAutoIncrement) {
            descriptors = (descriptors == null || descriptors.length() == 0) ? AUTOINCREMENT : descriptors + " " + AUTOINCREMENT;
        }
        if (!mIsPrimaryKey && !mNullable) {
            descriptors = (descriptors == null || descriptors.length() == 0) ? NOT_NULL : descriptors + " " + NOT_NULL;
        }
        return descriptors;
    }

    public String generateAddColumnSql(String databaseTableName) {
        checkState(((databaseTableName != null) && (databaseTableName.length() > 0)), "Must supply a table name to perform upgrade");
        return String.format("ALTER TABLE %s ADD %s", databaseTableName, getCreateClause());
    }
}
