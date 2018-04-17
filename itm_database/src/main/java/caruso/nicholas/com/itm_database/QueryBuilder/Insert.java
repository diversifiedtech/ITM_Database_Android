package caruso.nicholas.com.itm_database.QueryBuilder;

import android.content.ContentValues;

/**
 * Nick:1/9/2018
 * WorkOrder.
 */

public class Insert {

    private String table_name;
    private ContentValues record;
    private String col_id_key;
    public boolean withId = false;

    public Insert(String table_name, String col_id, ContentValues record) {
        this.table_name = table_name;
        this.col_id_key = col_id;
        this.record = record;
    }

    public String getTable() {
        return table_name;
    }

    public ContentValues getRecord() {
        if (!withId) {
            record.remove(col_id_key);
        }
        return record;
    }

    public Insert withId() {
        withId = true;
        return this;
    }

    public Insert withoutId() {
        withId = false;
        return this;
    }


}
