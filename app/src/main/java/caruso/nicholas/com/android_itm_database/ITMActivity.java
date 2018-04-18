package caruso.nicholas.com.android_itm_database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import caruso.nicholas.com.android_itm_database.database.item.User;

public class ITMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itm);

        User.getModal(this).getListOfUsers();

    }
}
