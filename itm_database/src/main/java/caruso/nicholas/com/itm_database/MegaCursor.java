package caruso.nicholas.com.itm_database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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

    public Double getDoubleByField(String fieldname) {
        if (this.getColumnIndex(fieldname) == -1) {
            return null;
        }
        if (this.isNull(this.getColumnIndex(fieldname))) {
            return null;
        }
        return this.getDouble(this.getColumnIndex(fieldname));
    }
//
//    public Object getObjectByField(String fieldname) throws IOException, ClassNotFoundException {
//        if (this.getColumnIndex(fieldname) == -1) {
//            return null;
//        }
//        if (this.isNull(this.getColumnIndex(fieldname))) {
//            return null;
//        }
//
//        ByteArrayInputStream in = new ByteArrayInputStream(this.getBlob(this.getColumnIndex(fieldname)));
//        ObjectInputStream is = new ObjectInputStream(in);
//        return is.readObject();
//    }

    public boolean empty() {
        return this.isAfterLast();
    }

}
