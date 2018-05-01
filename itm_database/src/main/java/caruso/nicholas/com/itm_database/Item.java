package caruso.nicholas.com.itm_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.MatrixCursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import caruso.nicholas.com.itm_database.QueryBuilder.Delete;
import caruso.nicholas.com.itm_database.QueryBuilder.Insert;
import caruso.nicholas.com.itm_database.QueryBuilder.Update;
import caruso.nicholas.com.itm_database.QueryBuilder.Where;

/**
 * Nick:1/11/2018
 * WorkOrder.
 */

public abstract class Item implements Serializable {
    public boolean newInstance;
    public static final int DELETE_MODE_CASCADE = 0x2;
    public static final int DELETE_MODE_NOCASCADE = 0x1;
    public static final int DELETE_MODE_DEFAULT = 0;

    public abstract String dump();

    public abstract ContentValues getRecord();

    public JSONObject getJSON(TableHelper tableHelper) {
        String[] fields = tableHelper.sync_up_filter_fields();
        JSONObject jsonObject = new JSONObject();
        ContentValues record = getRecord();
        for (String field : fields) {
            String rec = record.getAsString(field);
            try {
                jsonObject.put(field, rec != null ? rec : JSONObject.NULL);
            } catch (JSONException ignore) {
            }
        }
        return jsonObject;
    }


    protected static MegaCursor JSONToCursor(JSONObject jsonObject) {
        Iterator names = jsonObject.keys();
        ArrayList<String> matrix = new ArrayList<>();
        while (names.hasNext()) {
            matrix.add((String) names.next());
        }
        MatrixCursor cursor = new MatrixCursor(matrix.toArray(new String[matrix.size()]));
        MatrixCursor.RowBuilder s = cursor.newRow();
        for (String m : matrix) {
            try {
                s.add(m, jsonObject.get(m));
            } catch (JSONException ignore) {
                s.add(m, JSONObject.NULL);
            }
        }
        cursor.moveToFirst();
        return new MegaCursor(cursor);
    }

    public abstract boolean insert(Context context);

    public abstract boolean update(Context context);

    public abstract boolean delete(Context context, int mode);

    protected final boolean insert(DatabaseHelper db, String table, String
            column, ContentValues records) {
        Insert insert = new Insert(table, column, records);
        return db.megaInsert(insert);
    }

    protected final boolean insertWithId(DatabaseHelper db, String table, String
            column, ContentValues records) {
        Insert insert = new Insert(table, column, records);
        insert.withId();
        return db.megaInsert(insert);
    }

    protected final boolean update(DatabaseHelper db, String table, String
            column, ContentValues records, int id) {
        return update(db, table, column, records, id + "");
    }

    protected final boolean update(DatabaseHelper db, String table, String
            column, ContentValues records, String id) {
        Where where = new Where(column, "=", id);
        Update update = new Update(table, records, where);
        return db.megaUpdate(update);
    }

    protected final boolean delete(DatabaseHelper db, String table, String column, int id) {
        return delete(db, table, column, id + "");
    }

    protected final boolean delete(DatabaseHelper db, String table, String column, String id) {
        Where where = new Where(column, "=", id);
        Delete delete = new Delete(table, where);
        return db.megaDelete(delete);
    }
}
