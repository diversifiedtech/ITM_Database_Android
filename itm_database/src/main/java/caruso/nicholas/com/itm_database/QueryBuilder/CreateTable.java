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
}