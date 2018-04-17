package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;

/**
 * Nick:1/5/2018
 * WorkOrder.
 */


public class WhereClump implements WhereWrapper {
    private WhereWrapper Left;
    private WhereWrapper Right;
    private String Operation;

    public WhereClump(WhereWrapper left, String operator, WhereWrapper right) {
        Left = left;
        Right = right;
        Operation = operator;
    }

    public WhereClump(WhereWrapper left, boolean operator, WhereWrapper right) {
        Left = left;
        Right = right;
        Operation = operator ? "AND" : "OR";
    }


    public WhereObject getWhereObject() {
        String whereStatement;
        ArrayList<String> whereArgs = new ArrayList<>();
        WhereObject rightWhere = (Right != null) ? Right.getWhereObject() : WhereObject.NULL;
        WhereObject leftWhere = (Left != null) ? Left.getWhereObject() : WhereObject.NULL;

        if (rightWhere == WhereObject.NULL || leftWhere == WhereObject.NULL) {
            if (rightWhere == WhereObject.NULL && leftWhere == WhereObject.NULL) {
                return WhereObject.NULL;
            } else if (rightWhere == WhereObject.NULL) {
                whereStatement = leftWhere.getStatement();
                whereArgs.addAll(leftWhere.getArgs());
            } else {
                whereStatement = rightWhere.getStatement();
                whereArgs.addAll(rightWhere.getArgs());
            }
        } else {
            whereStatement = "(" + leftWhere.getStatement() + " " + Operation + " " + rightWhere.getStatement() + ")";
            whereArgs.addAll(leftWhere.getArgs());
            whereArgs.addAll(rightWhere.getArgs());

        }
        return new WhereObject(whereStatement, whereArgs);
    }


}

