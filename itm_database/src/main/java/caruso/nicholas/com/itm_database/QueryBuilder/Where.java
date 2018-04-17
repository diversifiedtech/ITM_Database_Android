package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Nick:1/5/2018
 * WorkOrder.
 */

public class Where implements WhereWrapper {
    private String where;
    private int method;
    private String whereArgs;

    public Where(String where, String method, String whereArgs) {
        this.where = where;
        this.whereArgs = whereArgs;
        this.method = getMethod(method);
    }

    public Where(String where, int method, String whereArgs) {
        this.where = where;
        this.whereArgs = whereArgs;
        this.method = getMethod(method);
    }

    public Where(String where, String method, int whereArgs) {
        this(where, method, whereArgs + "");
    }

    public Where(String where, int method, int whereArgs) {
        this(where, method, whereArgs + "");
    }

    private int getMethod(String method) {
        int methodIndex = Arrays.asList(methods).indexOf(method);
        return getMethod(methodIndex);
    }

    private int getMethod(int index) {
        if (index >= 0 && index < methods.length) {
            return index;
        }
        return 0;
    }

    public String getWhere() {
        return where;
    }

    public String getMethod() {
        return methods[method];
    }


    private static String[] methods = {
            "=",
            "<>",
            "!=",
            "<",
            ">",
            "<=",
            ">=",
            "BETWEEN",
            "EXISTS",
            "IN",
            "LIKE",
            "NOT BETWEEN",
            "NOT EXISTS",
            "NOT IN",
            "NOT LIKE"
    };

    @Override
    public WhereObject getWhereObject() {
        if (where == null || whereArgs == null) {
            return WhereObject.NULL;
        }
        String whereStatement = " " + where + " " + methods[method] + " ? ";
        ArrayList<String> whereArguments = new ArrayList<>();
        whereArguments.add(whereArgs);
        return new WhereObject(whereStatement, whereArguments);
    }

    public boolean equals(Where where) {
        return this.where.equals(where.where) && this.whereArgs.equals(where.whereArgs) && this.method == where.method;
    }
}
