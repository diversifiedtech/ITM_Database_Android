package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;

/**
 * Nick:1/17/2018
 * WorkOrder.
 */

public class Delete {

    String table_name;
    WhereWrapper where;

    public Delete(String table_name, WhereWrapper where) {
        this.table_name = table_name;
        this.where = where;
    }

    public String getTable() {
        return table_name;
    }

    public DeleteObject getWhere() {
        WhereWrapper.WhereObject wr = where.getWhereObject();
        return new DeleteObject(wr.getStatement(), wr.getArgs());
    }

    public class DeleteObject {
        private String whereStatement;
        private ArrayList<String> whereArgs;

        DeleteObject(String where, ArrayList<String> whereArgs) {
            this.whereStatement = where;
            this.whereArgs = whereArgs;
        }

        public String getStatement() {
            return whereStatement;
        }

        public String[] getWhereArgs() {
            return whereArgs.toArray(new String[whereArgs.size()]);
        }
    }

}