package caruso.nicholas.com.android_itm_database.database.item;

import android.content.ContentValues;
import android.content.Context;

import java.util.Date;

import caruso.nicholas.com.android_itm_database.database.Database;
import caruso.nicholas.com.android_itm_database.database.modal.UserModal;
import caruso.nicholas.com.android_itm_database.database.table.UsersTable;
import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.DateTimeConversion;
import caruso.nicholas.com.itm_database.Item;
import caruso.nicholas.com.itm_database.MegaCursor;

/**
 * Nick:1/4/2018
 * WorkOrder.
 */

public class User extends Item {

    public Integer userid;
    public String username;
    public String password;
    public Integer companyno;
    public String name;
    public Date Last_Sync;

    public String Last_SyncString() {
        return DateTimeConversion.TimeMedium(Last_Sync);
    }

    public void Last_Sync(String last_sync) {
        Last_Sync = DateTimeConversion.StringToDateTime(last_sync);
    }

    public static UserModal getModal(Context mContext) {
        return new UserModal(mContext);
    }

    public User(MegaCursor cursor) {
        super();
        userid = cursor.getIntByField(UsersTable.COL_ID);
        username = cursor.getStringByField(UsersTable.COL_USERNAME);
        password = cursor.getStringByField(UsersTable.COL_PASSWORD);
        companyno = cursor.getIntByField(UsersTable.FIELD_COMPANYNO);
        name = cursor.getStringByField(UsersTable.FIELD_NAME);
        Last_Sync(cursor.getStringByField(UsersTable.FIELD_LAST_SYNC));
    }

    @Override
    public String dump() {
        String dump = "User dump:\n";
        dump += "userid" + userid + "\n";
        dump += "username" + username + "\n";
        dump += "password" + password + "\n";
        dump += "companyno" + companyno + "\n";
        dump += "name" + name + "\n";
        dump += "Last_Sync" + Last_SyncString() + "\n";
        return dump;
    }

    @Override
    public ContentValues getRecord() {
        ContentValues cv = new ContentValues();
        cv.put(UsersTable.COL_ID, userid);
        cv.put(UsersTable.COL_USERNAME, username);
        cv.put(UsersTable.COL_PASSWORD, password);
        cv.put(UsersTable.FIELD_COMPANYNO, companyno);
        cv.put(UsersTable.FIELD_NAME, name);
        cv.put(UsersTable.FIELD_LAST_SYNC, Last_SyncString());
        return cv;
    }

    @Override
    public boolean insert(Context context) {
        DatabaseHelper db = new Database(context);
        boolean i = insert(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, getRecord());
        db.close();
        return i;
    }

    @Override
    public boolean update(Context context) {
        DatabaseHelper db = new Database(context);
        boolean i = update(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, getRecord(), userid);
        db.close();
        return i;
    }

    @Override
    public boolean delete(Context context, int mode) {
        DatabaseHelper db = new Database(context);
        boolean i = delete(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, userid);
        db.close();
        return i;
    }
}
