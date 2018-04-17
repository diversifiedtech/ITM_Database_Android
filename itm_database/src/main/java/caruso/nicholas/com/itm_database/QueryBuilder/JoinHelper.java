package caruso.nicholas.com.itm_database.QueryBuilder;

/**
 * Nick:1/5/2018
 * WorkOrder.
 */

public class JoinHelper {
    private String table;
    private JoinHelper prevTable = null;
    private JoinHelper nextTable = null;
    private String join_key_prev = null;
    private Integer joinMethod = null;
    private String join_key = null;

    /*
    There is a need to make the joins able to be added in multiple directions like a tree
    workorder to customer and workorder to operation etc.
     */
    public JoinHelper(String table) {
        this.table = table;
    }

    public static JoinHelper initJoin(String table) {
        return new JoinHelper(table);
    }

    public String getTable() {
        return table;
    }

    public JoinHelper makeJoin(String table, String local_key, String foreign_key) {
        JoinHelper j = new JoinHelper(table);
        j.makeJoin(this, local_key, 0);
        this.join_key = foreign_key;
        this.nextTable = j;
        return j;
    }

    public JoinHelper makeJoin(String table, String local_key, String foreign_key, int method) {
        JoinHelper j = new JoinHelper(table);
        j.makeJoin(this, local_key, method);
        this.join_key = foreign_key;
        this.nextTable = j;
        return j;
    }

    private void makeJoin(JoinHelper previousTable, String prevJoin, int method) {
        this.prevTable = previousTable;
        if (method != 1) {
            method = 0;
        }
        joinMethod = method;
        this.join_key_prev = prevJoin;
        this.joinMethod = method;
    }

    private String getJoinString() {
        if (prevTable == null) {
            return table;
        } else {
            String joinQuery = " " + joinMethods[this.joinMethod];
            String table_field_prev = this.prevTable.table + "." + this.prevTable.join_key;
            String table_field_this = this.table + "." + this.join_key_prev;
            joinQuery += " " + this.table + " ON " + table_field_prev + " = " + table_field_this + " ";
            return joinQuery;
        }

    }

    public JoinHelper getNext() {
        if (this.nextTable == null) {
            return null;
        }
        return this.nextTable;
    }

    public JoinHelper getHead() {
        if (this.prevTable == null) {
            return this;
        }
        return this.prevTable.getHead();
    }

    public String getFullJoin() {
        if (this.nextTable == null) {
            return this.getJoinString();
        }
        return this.getJoinString() + this.nextTable.getFullJoin();
    }

    private static String[] joinMethods = new String[]{"INNER JOIN", "LEFT OUTER JOIN"};
}
