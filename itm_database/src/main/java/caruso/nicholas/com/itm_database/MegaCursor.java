package caruso.nicholas.com.itm_database;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Nick:9/28/2017
 * WorkOrder.
 */

public class MegaCursor extends CursorWrapper {
    public MegaCursor(Cursor cursor) {
        super(cursor);
    }

    public String getStringByField(String fieldname) {
        if (this.getColumnIndex(fieldname) == -1) {
            return null;
        }
        if (this.isNull(this.getColumnIndex(fieldname))) {
            return null;
        }
        return this.getString(this.getColumnIndex(fieldname));
    }

    public Integer getIntByField(String fieldname) {
        if (this.getColumnIndex(fieldname) == -1) {
            return null;
        }
        if (this.isNull(this.getColumnIndex(fieldname))) {
            return null;
        }
        return this.getInt(this.getColumnIndex(fieldname));
    }

    public boolean empty() {
        return this.isAfterLast();
    }

}
