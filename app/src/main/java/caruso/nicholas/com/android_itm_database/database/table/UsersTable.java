package caruso.nicholas.com.android_itm_database.database.table;

import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.CreateTable;
import caruso.nicholas.com.itm_database.QueryBuilder.Where;
import caruso.nicholas.com.itm_database.TableHelper;

/**
 * Created by Nick on 10/3/2017
 * as a part of WorkOrder.
 */

public class UsersTable extends TableHelper {

    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "userid";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String[] FIELDS = {
            COL_ID,
            COL_USERNAME,
            COL_PASSWORD,
    };

    @Override
    public String[] sync_up_filter_fields() {
        return new String[]{COL_ID};
    }

    public UsersTable(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }

    @Override
    public boolean sync_up() {
        return false;
    }

    @Override
    public boolean sync_down() {
        return false;
    }


    @Override
    public Where sync_up_condition() {
        return SYNC_NONE;
    }


    @Override
    public String table_name() {
        return TABLE_NAME;
    }

    @Override
    public String id_column() {
        return COL_ID;
    }

    @Override
    public String[] fields() {
        return FIELDS;
    }

    public String CREATE_TABLE() {
        //Create the second table
        CreateTable user = new CreateTable(TABLE_NAME);
        user.put(COL_ID, INT(11), PRIMARY_KEY);
        user.put(COL_USERNAME, VARCHAR(20));
        user.put(COL_PASSWORD, VARCHAR(20));
        return user.get();
    }
}
