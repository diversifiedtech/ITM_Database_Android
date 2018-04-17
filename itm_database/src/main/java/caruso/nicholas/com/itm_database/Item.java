package caruso.nicholas.com.itm_database;

import android.content.ContentValues;
import android.content.Context;

import java.io.Serializable;

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


    public Item() {
    }

    public abstract String dump();

    public abstract ContentValues getRecord();

    public abstract boolean insert(Context context);

    public abstract boolean update(Context context);

    public abstract boolean delete(Context context, int mode);

    protected final boolean insert(DatabaseHelper db, String table, String column, ContentValues records) {
        Insert insert = new Insert(table, column, records);
        return db.megaInsert(insert);
    }

    protected final boolean update(DatabaseHelper db, String table, String column, ContentValues records, int id) {
        return update(db, table, column, records, id + "");
    }

    protected final boolean update(DatabaseHelper db, String table, String column, ContentValues records, String id) {
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
