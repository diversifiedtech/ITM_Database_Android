package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;

/**
 * Nick:1/5/2018
 * WorkOrder.
 */

public interface WhereWrapper {
    WhereObject getWhereObject();


    class WhereObject {
        private String whereStatement;
        private ArrayList<String> whereArgs;

        WhereObject(String x, ArrayList<String> y) {
            whereStatement = x;
            whereArgs = y;
        }

        public String getStatement() {
            return whereStatement;
        }

        public ArrayList<String> getArgs() {
            return whereArgs;
        }

        public static WhereObject NULL = new WhereObject("", null);
    }

}
