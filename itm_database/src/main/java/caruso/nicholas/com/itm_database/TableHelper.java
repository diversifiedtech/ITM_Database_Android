package caruso.nicholas.com.itm_database;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import caruso.nicholas.com.itm_database.QueryBuilder.CreateTable;
import caruso.nicholas.com.itm_database.QueryBuilder.DropTable;
import caruso.nicholas.com.itm_database.QueryBuilder.Insert;
import caruso.nicholas.com.itm_database.QueryBuilder.JoinHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.OrderList;
import caruso.nicholas.com.itm_database.QueryBuilder.ProjectionList;
import caruso.nicholas.com.itm_database.QueryBuilder.Query;
import caruso.nicholas.com.itm_database.QueryBuilder.Truncate;
import caruso.nicholas.com.itm_database.QueryBuilder.Where;


/**
 * Nick:9/8/2017
 * WorkOrder.
 */

public abstract class TableHelper implements Serializable {
    private DatabaseHelper dbHelper;
    public static final Where SYNC_NONE = new Where("1", "=", "0");
    public static final Where SYNC_ALL = new Where("1", "=", "1");

    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String AUTOINCREMENT = "AUTOINCREMENT";
    public static final String UNIQUE = "UNIQUE";
    public static final String INTEGER = "INTEGER";
    public static final String TEXT = "TEXT";
    public static final String DATETIME = "DATETIME";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";

    public static String TINYINT(int num) {
        return "TINYINT(" + num + ")";
    }

    public static String INT(int num) {
        return "INT(" + num + ")";
    }

    public static String VARCHAR(int num) {
        return "VARCHAR(" + num + ")";
    }

    public abstract boolean sync_up();

    public abstract boolean sync_down();

    public abstract Where sync_up_condition();

    public abstract String table_name();

    public abstract String id_column();

    public abstract String[] fields();

    public abstract String[] sync_up_filter_fields();

    public abstract String CREATE_TABLE();

    public TableHelper(DatabaseHelper databaseHelper) {
        dbHelper = databaseHelper;
    }

    public void truncate() {
        Truncate truncate = new Truncate(table_name());
        dbHelper.megaSafeTruncate(truncate.ImSure());
    }

    public void drop() {
        DropTable dropTable = new DropTable(table_name());
        dbHelper.megaSafeDropTable(dropTable.ImSure());
    }

    public String getJSONPOST() throws JSONException {
        MegaCursor cursor;
        ProjectionList projectionList = new ProjectionList();
        Where where;
        OrderList orderList = new OrderList();
        if (sync_up_condition().equals(SYNC_NONE)) {
            return "";
        } else if (sync_up_condition().equals(SYNC_ALL)) {
            where = null;
        } else {
            where = sync_up_condition();
        }
        projectionList.addAll(sync_up_filter_fields());
        orderList.add(id_column());
        Query query = new Query(projectionList, JoinHelper.initJoin(table_name()), where, null);
        cursor = dbHelper.megaSelect(query);

        JSONArray jsonTable = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            JSONObject jsonRecord = new JSONObject();
            for (String field : projectionList.getColumns()) {
                String record = cursor.getStringByField(field);
                jsonRecord.put(field, record != null ? record : JSONObject.NULL);
            }
            jsonTable.put(jsonRecord);
            cursor.moveToNext();
        }
        cursor.close();
        return jsonTable.toString();
    }

    public void insertFromJSON(JSONArray jsonTable) throws JSONException {
        ContentValues row = new ContentValues();
        for (int i = 0; i < jsonTable.length(); i++) {
            JSONObject jsonRecord = jsonTable.getJSONObject(i);
            for (String field : fields()) {
                String rec = jsonRecord.getString(field);
                if (!rec.equalsIgnoreCase("null")) {
                    row.put(field, rec);
                } else {
                    row.putNull(field);
                }
            }
            Insert insert = new Insert(table_name(), id_column(), row).withId();
            dbHelper.megaInsert(insert);
        }
    }

}
