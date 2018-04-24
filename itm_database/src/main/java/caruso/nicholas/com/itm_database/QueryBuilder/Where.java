package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Nick:1/5/2018
 * WorkOrder.
 */

public class Where implements WhereWrapper {
    private static final int METHOD_BETWEEN = 7;
    private static final int METHOD_EXISTS = 8;
    private static final int METHOD_IN = 9;
    private static final int METHOD_IS_NULL = 15;
    private static final int METHOD_NOT_BETWEEN = 11;
    private static final int METHOD_NOT_EXISTS = 12;
    private static final int METHOD_NOT_IN = 13;
    private static final int METHOD_IS_NOT_NULL = 16;

    private String field;
    private int method;
    private String[] whereArgs;

    private Where() {
    }

    public static Where isNull(String field, boolean isNull) {
        Where where = new Where();
        where.field = field;
        where.method = isNull ? METHOD_IS_NULL : METHOD_IS_NOT_NULL;
        where.whereArgs = new String[]{"NULL"};
        return where;
    }


    public static Where Between(String field, boolean isBetween, double low, double high) {
        Where where = new Where();
        where.field = field;
        where.method = isBetween ? METHOD_BETWEEN : METHOD_NOT_BETWEEN;
        where.whereArgs = new String[]{low + "", high + ""};
        return where;
    }

    public static Where In(String field, boolean isIn, double[] doubles) {
        Where where = new Where();
        where.field = field;
        where.method = isIn ? METHOD_IN : METHOD_NOT_IN;
        where.whereArgs = new String[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            where.whereArgs[i] = doubles[i] + "";
        }
        return where;
    }

    public static Where In(String field, boolean isIn, String[] strings) {
        Where where = new Where();
        where.field = field;
        where.method = isIn ? METHOD_IN : METHOD_NOT_IN;
        where.whereArgs = strings;
        return where;
    }


    public Where(String field, String method, String whereArgs) {
        this.field = field;
        this.whereArgs = new String[1];
        this.whereArgs[0] = whereArgs;
        this.method = getMethod(method);
    }


    public Where(String field, int method, String whereArgs) {
        this.field = field;
        this.whereArgs = new String[1];
        this.whereArgs[0] = whereArgs;
        this.method = getMethod(method);
    }

    public Where(String field, String method, int whereArgs) {
        this(field, method, whereArgs + "");
    }

    public Where(String field, int method, int whereArgs) {
        this(field, method, whereArgs + "");
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
        return field;
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
            "NOT LIKE",
            "IS NULL",
            "IS NOT NULL"
    };

    @Override
    public WhereObject getWhereObject() {
        if (field == null || whereArgs == null) {
            return WhereObject.NULL;
        }
        if (method == METHOD_IS_NULL || method == METHOD_IS_NOT_NULL) {
            return getIsNullWO(method == METHOD_IS_NULL);
        }
        if (method == METHOD_BETWEEN || method == METHOD_NOT_BETWEEN) {
            return getBetweenWO(method == METHOD_BETWEEN);
        }
        if (method == METHOD_IN || method == METHOD_NOT_IN) {
            return getInWO(method == METHOD_IN);
        }
        String whereStatement = " " + field + " " + methods[method] + " ? ";
        ArrayList<String> whereArguments = new ArrayList<>(Arrays.asList(whereArgs));
        return new WhereObject(whereStatement, whereArguments);
    }

    private WhereObject getIsNullWO(boolean isNull) {
        String whereStatement = " " + field + " " + (isNull ? methods[METHOD_IS_NULL] : methods[METHOD_IS_NOT_NULL]) + " ";
        ArrayList<String> whereArguments = new ArrayList<>();
        return new WhereObject(whereStatement, whereArguments);
    }

    private WhereObject getBetweenWO(boolean isBetween) {
        String whereStatement = " " + field + " " + (isBetween ? methods[METHOD_BETWEEN] : methods[METHOD_NOT_BETWEEN]) + " ? AND ? ";
        ArrayList<String> whereArguments = new ArrayList<>(Arrays.asList(whereArgs));
        return new WhereObject(whereStatement, whereArguments);
    }

    private WhereObject getInWO(boolean isIn) {
        StringBuilder whereStatement = new StringBuilder(" " + field + " " +(isIn ? methods[METHOD_IN] : methods[METHOD_NOT_IN]) + " (");
        for (int i = 0; i < whereArgs.length; i++) {
            if (i != 0) {
                whereStatement.append(",");
            }
            whereStatement.append("?");
        }
        whereStatement.append(")");
        ArrayList<String> whereArguments = new ArrayList<>(Arrays.asList(whereArgs));
        return new WhereObject(whereStatement.toString(), whereArguments);

    }
    //Coming Soon
//    private WhereObject getExistWO() {
//
//    }

    public boolean equals(Where where) {
        return this.field.equals(where.field) && Arrays.equals(this.whereArgs, where.whereArgs) && this.method == where.method;
    }
}
