package caruso.nicholas.com.android_itm_database.database.modal;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import caruso.nicholas.com.android_itm_database.database.Database;
import caruso.nicholas.com.android_itm_database.database.item.User;
import caruso.nicholas.com.android_itm_database.database.table.UsersTable;
import caruso.nicholas.com.itm_database.DatabaseHelper;
import caruso.nicholas.com.itm_database.ItemModal;
import caruso.nicholas.com.itm_database.MegaCursor;
import caruso.nicholas.com.itm_database.QueryBuilder.Insert;
import caruso.nicholas.com.itm_database.QueryBuilder.JoinHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.ProjectionList;
import caruso.nicholas.com.itm_database.QueryBuilder.Query;
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

    public boolean restoreFromList(List<User> users) {
        DatabaseHelper db = new Database(mContext);
        List<Insert> insertList = new ArrayList<>();
        for (User user : users) {
            Insert insert = new Insert(table, id, user.getRecord());
            insertList.add(insert);
            db.megaInsert(insert);
        }
        return db.megaInsert(insertList);
    }

    public User getUserById(Integer user_id) {
        DatabaseHelper db = new Database(mContext);
        Where where = new Where(id, "=", user_id + "");
        Query query = new Query(null, new JoinHelper(table), where, null);
        MegaCursor cursor = db.megaSelect(query);
        cursor.moveToFirst();
        return new User(cursor);
    }

    public List<User> getUsersByUsername(String Username) {
        DatabaseHelper db = new Database(mContext);
        Query query = getUsersByUsernameQuery(Username);
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


    private Query getUsersByUsernameQuery(String Username) {
        JoinHelper joinHelper = new JoinHelper(table);
        Where where = new Where(UsersTable.COL_USERNAME, "=", Username);
        return new Query(null, joinHelper, where, null);
    }

    public Integer getUserIdByPassword(List<User> matching_users, String mPassword) {
        for (User user : matching_users) {
            if (user.password.equals(mPassword)) {
                return user.userid;
            }
        }
        return null;
    }

    public String getUserIdByPasswordCursor(List<User> matching_users, String mPassword) {
        DatabaseHelper db = new Database(mContext);
        Query subquery = getUsersByUsernameQuery(matching_users.get(0).username);
        JoinHelper join = new JoinHelper(subquery.getQueryAS("User"));
        Where where = new Where(UsersTable.COL_PASSWORD, "=", mPassword);
        ProjectionList projectionList = new ProjectionList();
        projectionList.add(id);
        Query query = new Query(projectionList, join, where, null);
        MegaCursor cursor = db.megaSelect(query);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            return null;
        }
        return cursor.getStringByField(projectionList.getColumns()[0]);
    }

}
