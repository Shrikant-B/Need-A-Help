package com.shrikantbadwaik.needahelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void reportIncident(View view)
    {
        startActivity(new Intent(this, ReportIncidentActivity.class));
        finish();
    }

    public void helpMe(View view)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);

        if((preferences.contains("Number_1")||preferences.contains("Number_2")||preferences.contains("Number_3")||preferences.contains("Number_4"))&&(preferences1.contains("Message"))){
            startActivity(new Intent(this,HelpMeActivity.class));
            finish();
        }else {
            startActivity(new Intent(this, EmergencyContactActivity.class));
            finish();
        }
    }

    public void findMe(View view)
    {
        startActivity(new Intent(this,FindMeActivity.class));
        finish();
    }

    public void policePhoneDirectory(View view)
    {
        startActivity(new Intent(this,PhoneDirectoryActivity.class));
        finish();
    }

    public void myReports(View view)
    {
        startActivity(new Intent(this,MyReportsActivity.class));
        finish();
    }

    public void aboutUs(View view)
    {
        startActivity(new Intent(this,AboutUsActivity.class));
        finish();
    }
}
