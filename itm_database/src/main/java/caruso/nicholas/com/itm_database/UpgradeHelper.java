package caruso.nicholas.com.itm_database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import caruso.nicholas.com.itm_database.QueryBuilder.Delete;
import caruso.nicholas.com.itm_database.QueryBuilder.JoinHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.ProjectionList;
import caruso.nicholas.com.itm_database.QueryBuilder.Query;

/**
 * Nick:12/11/2017
 * WorkOrder.
 */

public abstract class UpgradeHelper {
//    int[] versions = {100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112};
    protected SQLiteDatabase db;
    protected int oldVersion;
    protected int newVersion;

    protected UpgradeHelper(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public abstract void upgrade(DatabaseHelper database);

    protected ArrayList<String> newGeneratedTables = new ArrayList<>();

    protected boolean isNewlyGeneratedTable(String table_name) {
        //if a table is newly generated on this upgrade version then you don't need to run any of the upgrade commands
        //This is because the table will be generated as the current versions table.
        return newGeneratedTables.contains(table_name);
    }

    protected void addTable(TableHelper tableHelper) {
        //Add this newly created table to the array so we can check it later
        newGeneratedTables.add(tableHelper.table_name());
        db.execSQL(tableHelper.CREATE_TABLE());
    }

    protected void addColumn(String tableName, String columnName, String datatype, String Default, boolean nullable) {
        String alterTable = "ALTER TABLE " + tableName;
        alterTable += " ADD " + columnName + " " + datatype + " ";
        alterTable += nullable ? "NULL" : " NOT NULL";
        if (Default != null) {
            alterTable += " DEFAULT " + Default;
        }
        db.execSQL(alterTable);
    }

    protected void dropColumn(String table_name, String columnName) {
        //Start transaction
        db.beginTransaction();

        //rename Table
        db.execSQL("ALTER TABLE " + table_name + " RENAME TO " + "temp_" + table_name);

        //recreate table
        Cursor table_fields = db.rawQuery("PRAGMA table_info(" + "temp_" + table_name + ")", null);
//        String create_table = " CREATE TABLE table_name "
        String create_table = createTableWithoutField(table_fields, table_name, new String[]{columnName});
        db.execSQL(create_table);

        //Build Transfer Query
        Cursor dbCursor = db.query("temp_" + table_name, null, null, null, null, null, null);
        String[] columns = dbCursor.getColumnNames();
        dbCursor.close();

        ProjectionList projectionList = new ProjectionList();
        for (String field : columns) {
            if (!field.equals(columnName)) {
                projectionList.add("temp_" + table_name, field);
            }
        }
        Query query = new Query(projectionList, new JoinHelper("temp_" + table_name), null, null);
        String transferQuery = "INSERT INTO " + table_name + " " + query.getQuery().getQuery();

        //Execute Transfer Query
        db.execSQL(transferQuery);

        //DROP temp table
        db.execSQL("DROP TABLE IF EXISTS " + "temp_" + table_name);


        db.setTransactionSuccessful();
        db.endTransaction();
    }

    protected void toTempTable(String table_name) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name + "_temp");
        db.execSQL("ALTER TABLE " + table_name + " RENAME TO " + table_name + "_temp" + ";");
        Cursor c = db.rawQuery("SELECT * FROM " + table_name + "_temp", null);
    }

    protected String createTableWithoutField(Cursor table_info, String table_name, String[] ignore) {
        table_info.moveToFirst();
        StringBuilder create_table = new StringBuilder("CREATE TABLE " + table_name + " (");
        boolean mutli = false;
        while (!table_info.isAfterLast()) {
            if (!Arrays.asList(ignore).contains(table_info.getString(1))) {
                if (mutli) {
                    create_table.append(",");
                }
                create_table.append(" ").append(table_info.getString(1));
                create_table.append(" ").append(table_info.getString(2));
                if (table_info.getInt(5) == 1) {
                    create_table.append(" PRIMARY KEY");
                }
                create_table.append(table_info.getInt(3) == 0 ? " NULL" : " NOT NULL");
                if (table_info.getString(4) != null) {
                    create_table.append(" DEFAULT ").append(table_info.getString(4));
                }
                mutli = true;
            }
            table_info.moveToNext();

        }
        create_table.append(");");
        return create_table.toString();

    }
//
//    private void copyPasteMove(TableHelper helper) {
//        toTempTable(helper.table_name());
//        db.execSQL(helper.CREATE_TABLE());
//        transferDataFromTemp(helper.table_name(), helper);
//        db.execSQL("DROP TABLE IF EXISTS " + helper.table_name() + "_temp");
//
//    }
//
//    private void copyPasteMove(TableHelper helper, String CREATE_STRING) {
//        toTempTable(helper.table_name());
//        db.execSQL(CREATE_STRING);
//        transferDataFromTemp(helper.table_name(), helper);
//        db.execSQL("DROP TABLE IF EXISTS " + helper.table_name() + "_temp");
//
//    }

    protected void transferDataFromTemp(String table_name, TableHelper helper) {
        String insertInto = "INSERT INTO " + table_name + "(";
        StringBuilder col = new StringBuilder();
        for (String field : helper.fields()) {
            col.append(",").append(field);
        }
        col = new StringBuilder(col.substring(1));
        insertInto += col + ")";
        insertInto += " SELECT " + col + " FROM " + table_name + "_temp";
        db.execSQL(insertInto);

    }


}
