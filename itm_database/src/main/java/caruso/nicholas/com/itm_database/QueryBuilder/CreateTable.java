package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;

/**
 * Nick:4/16/2018
 * AndroidStudioProjects.
 */
public class CreateTable {
    private String table_name;
    private StringBuilder create_string;
    private ArrayList<String> fields;
    private boolean ifNotExists = false;

    public CreateTable(String Table_name) {
        this.table_name = Table_name;
        fields = new ArrayList<>();
        create_string = new StringBuilder();
    }

    public void put(String... strings) {
        StringBuilder field = new StringBuilder();
        for (String s : strings) {
            field.append(s).append(" ");
        }
        fields.add(field.toString());
    }

    public void addIfNotExistsTag() {
        ifNotExists = true;
    }

    public void removeIfNotExistsTag() {
        ifNotExists = false;
    }

    public String get() {
        create_string.append("CREATE TABLE ").append(table_name);
        if (ifNotExists) {
            create_string.append(" IF NOT EXISTS");
        }
        create_string.append(" (");
        for (int i = 0; i < fields.size() - 1; i++) {
            create_string.append(fields.get(i)).append(",");
        }
        if (fields.size() != 0) {
            create_string.append(fields.get(fields.size() - 1));
        }
        create_string.append(");");
        return create_string.toString();
    }
    public static class ShortCuts{
        public static final Where SYNC_NONE = new Where("1", "=", "0");
        public static final Where SYNC_ALL = new Where("1", "=", "1");

        public static final String CREATE_TABLE = "CREATE TABLE";
        public static final String PRIMARY_KEY = "PRIMARY KEY";
        public static final String AUTOINCREMENT = "AUTOINCREMENT";
        public static final String UNIQUE = "UNIQUE";
        public static final String INTEGER = "INTEGER";
        public static final String TEXT = "TEXT";
        public static final String DATETIME = "DATETIME";
        public static final String DATE = "DATE";
        public static final String TIME = "TIME";
        public static final String BLOB = "BLOB";
        public static final String BOOLEAN = "BOOLEAN";
        public static final String REAL = "REAL";
        public static final String DEFAULT = "DEFAULT";
        public static final String NOT = "NOT";
        public static final String NOT_NULL = "NOT NULL";
        public static final String NULL = "NULL";

        public static String TINYINT(int num) {
            return "TINYINT(" + num + ")";
        }

        public static String INT(int num) {
            return "INT(" + num + ")";
        }

        public static String VARCHAR(int num) {
            return "VARCHAR(" + num + ")";
        }

        public static String DECIMAL(int num, int num2) {
            return "DECIMAL(" + num + "," + num2 + ")";
        }

        public static String NCHAR(int num) {
            return "NCHAR(" + num + ")";
        }

        public static String CHARACTER(int num) {
            return "CHARACTER(" + num + ")";
        }

        public static String DEFAULT(Object o) {
            return "DEFAULT " + o.toString();
        }
    }
}