package caruso.nicholas.com.itm_database.QueryBuilder;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Nick:1/9/2018
 * WorkOrder.
 */

public class Update {

    String table_name;
    ContentValues record;
    WhereWrapper where;

    public Update(String table_name, ContentValues record, WhereWrapper where) {
        this.table_name = table_name;
        this.record = record;
        this.where = where;
    }

    public String getTable() {
        return table_name;
    }

    public ContentValues getRecord() {
        return record;
    }

    public UpdateObject getWhere() {
        if (where == null) {
            return new UpdateObject(null, null);
        }
        WhereWrapper.WhereObject wr = where.getWhereObject();
        return new UpdateObject(wr.getStatement(), wr.getArgs());
    }

    public class UpdateObject {
        private String whereStatement;
        private ArrayList<String> whereArgs;

        UpdateObject(String where, ArrayList<String> whereArgs) {
            this.whereStatement = where;
            this.whereArgs = whereArgs;
        }

        public String getStatement() {
            return whereStatement;
        }

        public String[] getWhereArgs() {
            if (whereArgs == null) {
                return null;
            }
            return whereArgs.toArray(new String[whereArgs.size()]);
        }
    }

}
