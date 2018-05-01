package caruso.nicholas.com.android_itm_database.database.modal;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import caruso.nicholas.com.android_itm_database.database.Database;
import caruso.nicholas.com.android_itm_database.database.item.User;
import caruso.nicholas.com.android_itm_database.database.table.UsersTable;
import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.ItemModal;
import caruso.nicholas.com.itm_database.MegaCursor;
import caruso.nicholas.com.itm_database.QueryBuilder.JoinHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.Query;
import caruso.nicholas.com.itm_database.QueryBuilder.Truncate;
import caruso.nicholas.com.itm_database.QueryBuilder.Where;

/**
 * Nick:1/4/2018
 * WorkOrder.
 */

public class UserModal extends ItemModal {
    public UserModal(Context context) {
        super(context, UsersTable.TABLE_NAME, UsersTable.COL_ID);
    }

    public List<User> getListOfUsers() {
        DatabaseHelper db = new Database(mContext);
        Query query = new Query(null, new JoinHelper(table), null, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        return users;
    }

    public void truncate() {
        Truncate truncate = new Truncate(table);
        DatabaseHelper db = new Database(mContext);
        db.megaForceTruncate(truncate);
    }

    public void dummyData() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.userid = i;
            user.username = "username";
            if (i != 7) {
                user.password = "password";

            }
            if (i % 2 == 0) {
                user.blob = "Object String";
            } else {
                user.blob = new Simple(2, "Simple");
            }
            user.dub = 1.57;
            user.date = Calendar.getInstance().getTime();
            user.insert(mContext);
        }

    }

    public static class Simple {
        int x;
        String y;

        Simple(int x, String y) {
            this.x = x;
            this.y = y;
        }
    }

    public List<User> test() {
        DatabaseHelper db = new Database(mContext);
        Query query = new Query(null, new JoinHelper(table), null, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        Log.d("TAG", cursor.getCount() + "count ");
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
        Log.d("TAG", cursor.getCount() + "count ");
        return users;
    }

    public void testBetween() {
        DatabaseHelper db = new Database(mContext);
        Where where = Where.Between(UsersTable.COL_ID, true, 30, 40);
//        Where where = new Where(UsersTable.COL_ID,"=",30);
//        Where where2 = new Where(UsersTable.COL_ID, "<=",40);
//        WhereClump whereClump = new WhereClump(where,"OR",where2);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        Log.d("TAG", cursor.getCount() + "count ");
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }

    public void testNotBetween() {
        DatabaseHelper db = new Database(mContext);
        Where where = Where.Between(UsersTable.COL_ID, false, 30, 40);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }

    public void testIn() {
        DatabaseHelper db = new Database(mContext);
        double[] test = new double[]{1, 4, 17, 50};
        Where where = Where.In(id, true, test);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }

    public void testNotIn() {
        DatabaseHelper db = new Database(mContext);
        double[] test = new double[]{1, 4, 17, 50};
        Where where = Where.In(UsersTable.COL_ID, false, test);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }

    public void testIsNull() {
        DatabaseHelper db = new Database(mContext);
        Where where = Where.isNull(UsersTable.COL_PASSWORD, true);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }

    public void testIsNotNull() {
        DatabaseHelper db = new Database(mContext);
        Where where = Where.isNull(UsersTable.COL_PASSWORD, false);
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        List<User> users = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        for (User user : users) {
            Log.d("TAG", user.dump());
        }
    }
}
