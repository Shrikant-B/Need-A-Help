package com.shrikantbadwaik.needahelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class HelpMeActivity extends AppCompatActivity {

    String number_1,number_2,number_3,number_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void sendSOS(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences1=PreferenceManager.getDefaultSharedPreferences(this);
        number_1 = preferences.getString("Number_1", null);
        number_2 = preferences.getString("Number_2", null);
        number_3 = preferences.getString("Number_3", null);
        number_4 = preferences.getString("Number_4", null);

        String message=preferences1.getString("Message",null);
        String[] numberArray={number_1,number_2,number_3,number_4};

        SmsManager smsManager= SmsManager.getDefault();
        for (int i=0; i<numberArray.length;i++)
        {
            smsManager.sendTextMessage(numberArray[i],null,message,null,null);
        }
        Toast.makeText(HelpMeActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
