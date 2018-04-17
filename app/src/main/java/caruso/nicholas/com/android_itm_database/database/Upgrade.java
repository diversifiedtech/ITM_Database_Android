package caruso.nicholas.com.android_itm_database.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    public void upgrade(Context context) {

    }
}
