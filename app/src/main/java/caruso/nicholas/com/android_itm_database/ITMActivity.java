package caruso.nicholas.com.android_itm_database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import caruso.nicholas.com.android_itm_database.database.item.User;

public class ITMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itm);

        User.getModal(this).truncate();
        User.getModal(this).dummyData();

        User.getModal(this).testIsNull();
        User.getModal(this).testIsNotNull();

    }
}
