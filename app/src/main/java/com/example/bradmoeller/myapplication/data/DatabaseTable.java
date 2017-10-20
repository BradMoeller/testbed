package com.example.bradmoeller.myapplication.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.compat.BuildConfig;
import android.util.Log;


import java.util.ArrayList;
import java.util.Locale;

import static com.example.bradmoeller.myapplication.data.Preconditions.checkState;


public abstract class DatabaseTable<T> extends AbsVersionedObject implements DatabaseObject {

    public static final String COLUMN_ID = "_id";

    protected final ArrayList<DatabaseColumn> mColumns = new ArrayList<>();

    protected String mTableName;

    public DatabaseTable() {
        addColumn(new DatabaseColumn(COLUMN_ID, DatabaseColumn.DataType.INTEGER).isPrimaryKey(true));
    }

    public String generateCreateStatement() {

        checkState(mColumns.size() > 0, "Table columns not defined");

        return String.format(Locale.getDefault(), "CREATE TABLE %s (", mTableName) + generateColumnsClause() + ");";
    }

    public void addColumn(DatabaseColumn column) {
        mColumns.add(column);
    }


    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String name) {
        mTableName = name;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG) {
            Log.w("SQLITE UPGRADE", String.format("Upgrading table %s from version %s to %s", getTableName(), String.valueOf(oldVersion), String.valueOf(newVersion)));
        }
        for (DatabaseColumn column : mColumns) {
            if ((column.getVersionCode()) > oldVersion && (column.getVersionCode() <= newVersion)) {
                try {
                    database.execSQL(column.generateAddColumnSql(mTableName));
                }
                catch (SQLiteException sle) {
                    sle.printStackTrace();
                }
            }
        }
    }

    public DatabaseTable<T> setVersion(int majorVersion, int minorVersion, int patchVersion, int buildVersion) {
        setObjectVersion(majorVersion, minorVersion, patchVersion, buildVersion);
        return this;
    }

    protected String generateColumnsClause() {
        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < mColumns.size(); index++) {
            if (index > 0) {
                sb.append(",");
            }
            sb.append("\n");
            sb.append(mColumns.get(index).getCreateClause());
        }

        return sb.toString();
    }

    protected String getColumnNamesCommaSeperated(){
        String s = "";
        for(int i=0; i < mColumns.size(); i++){
            if(i != 0){
                s += ",";
            }
            s += mColumns.get(i).getColumnId();
        }
        return s;
    }
}
