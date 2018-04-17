package caruso.nicholas.com.android_itm_database.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import caruso.nicholas.com.android_itm_database.database.table.UsersTable;
import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.TableHelper;
import caruso.nicholas.com.itm_database.UpgradeHelper;

/**
 * Nick:4/16/2018
 * AndroidStudioProjects.
 */
public final class Database extends DatabaseHelper {
    public static final String REMOTE_DATABASE_LINK = "http://138.197.114.57";
    public static final String AuthTable = UsersTable.TABLE_NAME;
    public static final String DATABASE_NAME = "LocalDatabase";
    private static final int VERSION = 149;

    public Database(Context context) {
        super(context, REMOTE_DATABASE_LINK, DATABASE_NAME, null, VERSION);
    }

    @Override
    public ArrayList<TableHelper> all_tables() {
        ArrayList<TableHelper> TABLES = new ArrayList<>();
        TABLES.add(new UsersTable(this));
        return TABLES;
    }

    @Override
    protected UpgradeHelper getUpgradeHelper(SQLiteDatabase db, int oldVersion, int newVersion) {
        return new Upgrade(db, oldVersion, newVersion);
    }


}
