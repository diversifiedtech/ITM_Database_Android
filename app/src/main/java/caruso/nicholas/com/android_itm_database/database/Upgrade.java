package caruso.nicholas.com.android_itm_database.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import caruso.nicholas.com.itm_database.DatabaseHelper;
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
        Log.d("dtc", "upgrade");
        for (TableHelper t : database.all_tables()) {
            Log.d("dtc", t.table_name());
            DropTable dropTable = new DropTable(t.table_name());
            Database.megaSafeDropTable(dropTable.ImSure().ifExists(), db);
            Database.megaCreateTable(t.CREATE_TABLE(), db);
        }
    }
}
