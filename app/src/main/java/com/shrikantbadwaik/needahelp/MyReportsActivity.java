package com.shrikantbadwaik.needahelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import DBAdapter.ReportsAdapter;
import DBClasses.MyReports;

public class MyReportsActivity extends AppCompatActivity {

    private ListView listMyReports;
    ArrayList<MyReports>myReportsArrayList=new ArrayList<>();
    ReportsAdapter reportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        listMyReports=(ListView)findViewById(R.id.listReports);

        //myReportsArrayList.add("123");
        //myReportsArrayList.add("123");

        new FetchReportsTask().execute();

        reportsAdapter=new ReportsAdapter(this,myReportsArrayList);
        listMyReports.setAdapter(reportsAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    class FetchReportsTask extends AsyncTask<Void,Void,String>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MyReportsActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            String strUrl = "http://192.168.72.2:9090/NeedAHelp/GetReportStatusServlet";

            try {
                URL url = new URL(strUrl);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                StringBuilder builder = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                int ch;

                while ((ch = inputStreamReader.read())!= -1)
                {
                    builder.append((char)ch);
                }
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            myReportsArrayList.clear();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int index = 0 ; index < jsonArray.length(); index++)
                {
                    JSONObject object = jsonArray.getJSONObject(index);
                    MyReports myReports = new MyReports();

                    myReports.setReportname(object.getString("reportname"));
                    myReports.setReportdescription(object.getString("reportdescription"));
                    myReports.setLatitude(object.getString("latitude"));
                    myReports.setLongitude(object.getString("longitude"));
                    myReports.setReportstatus(object.getString("reportstatus"));

                    myReportsArrayList.add(myReports);
                }
                reportsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
