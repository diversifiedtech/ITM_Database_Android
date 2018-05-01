package caruso.nicholas.com.android_itm_database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import caruso.nicholas.com.android_itm_database.database.item.User;
import caruso.nicholas.com.android_itm_database.database.table.UsersTable;

public class ITMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itm);

        User.getModal(this).truncate();
        User.getModal(this).dummyData();
//
//        User.getModal(this).testIsNull();
//        User.getModal(this).testIsNotNull();

       List<User> users =  User.getModal(this).test();
       User user = users.get(0);
       JSONObject s = user.getJSON(new UsersTable());
       Log.d("TAG","JSON String " + s.toString());
       Log.d("TAG", "NEW USER " + new User(s).userid );


    }
}
