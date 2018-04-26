package caruso.nicholas.com.android_itm_database.database;

import android.database.sqlite.SQLiteDatabase;

import caruso.nicholas.com.android_itm_database.database.table.UsersTable;
import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.CreateTable;
import caruso.nicholas.com.itm_database.QueryBuilder.DropTable;
import caruso.nicholas.com.itm_database.TableHelper;
import caruso.nicholas.com.itm_database.UpgradeHelper;

/**
 * Nick:4/16/2018
 * AndroidStudioProjects.
 */
public class Upgrade extends UpgradeHelper {
    public Upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super(db, oldVersion, newVersion);
    }

    @Override
    public void upgrade(DatabaseHelper database) {
        switch (oldVersion) {
            case 2:
                addColumn(UsersTable.TABLE_NAME, UsersTable.FIELD_BLOB, CreateTable.ShortCuts.BLOB, null, true);
                addColumn(UsersTable.TABLE_NAME, UsersTable.FIELD_DOUBLE, CreateTable.ShortCuts.DECIMAL(2, 5), null, true);
                addColumn(UsersTable.TABLE_NAME, UsersTable.FIELD_DATETIME, CreateTable.ShortCuts.DATETIME, null, true);

            case 3:
                break;
            default:
                for (TableHelper t : database.all_tables()) {
                    DropTable dropTable = new DropTable(t.table_name());
                    Database.megaSafeDropTable(dropTable.ImSure().ifExists(), db);
                    Database.megaCreateTable(t.CREATE_TABLE(), db);
                }
        }

    }
}
