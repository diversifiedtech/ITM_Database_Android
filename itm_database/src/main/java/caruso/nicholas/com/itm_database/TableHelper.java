package caruso.nicholas.com.itm_database;

import android.content.ContentValues;
import android.content.Context;

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
import caruso.nicholas.com.itm_database.QueryBuilder.WhereWrapper;


/**
 * Nick:9/8/2017
 * WorkOrder.
 */

public abstract class TableHelper extends CreateTable.ShortCuts implements Serializable {
//    private DatabaseHelper dbHelper;

    public abstract boolean sync_up();

    public abstract boolean sync_down();

    public abstract WhereWrapper sync_up_condition();

    public abstract String table_name();

    public abstract String id_column();

    public abstract String[] fields();

    public abstract String[] sync_up_filter_fields();

    public abstract String CREATE_TABLE();

    public TableHelper() {

    }

    public void truncate(DatabaseHelper databaseHelper) {
        Truncate truncate = new Truncate(table_name());
        databaseHelper.megaSafeTruncate(truncate.ImSure());
    }

    public void drop(DatabaseHelper databaseHelper) {
        DropTable dropTable = new DropTable(table_name());
        databaseHelper.megaSafeDropTable(dropTable.ImSure());
    }

    public String getJSONPOST(DatabaseHelper databaseHelper) throws JSONException {
        MegaCursor cursor;
        ProjectionList projectionList = new ProjectionList();
        WhereWrapper where;
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
        cursor = databaseHelper.megaSelect(query);

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

    public void insertFromJSON(JSONArray jsonTable, DatabaseHelper databaseHelper) throws JSONException {
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
            databaseHelper.megaInsert(insert);
        }
    }

}
