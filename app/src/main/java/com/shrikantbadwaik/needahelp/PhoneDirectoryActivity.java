package com.shrikantbadwaik.needahelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import DBAdapter.DirectoryAdapter;
import DBClasses.PhoneDirectory;

public class PhoneDirectoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView directoryListView;
    ArrayList<PhoneDirectory> directoryList = new ArrayList<>();
    DirectoryAdapter directoryAdapter;
    TextView textContactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_directory);

        textContactNo = (TextView) findViewById(R.id.textContactNo);

        directoryListView = (ListView) findViewById(R.id.directoryListView);
        directoryListView.setOnItemClickListener(this);

        new FetchContactsTask().execute();

        directoryAdapter = new DirectoryAdapter(this, directoryList);
        directoryListView.setAdapter(directoryAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhoneDirectory directory = directoryList.get(position);
        String number = directory.getContactNumber();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel://" + number));
        startActivity(callIntent);
    }

    class FetchContactsTask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PhoneDirectoryActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String strUrl = "http://192.168.43.83:9090/NeedAHelp/GetContactNoServlet";

            try {
                URL url = new URL(strUrl);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                StringBuilder builder = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                int ch;

                while ((ch = inputStreamReader.read()) != -1) {
                    builder.append((char) ch);
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
            directoryList.clear();

            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject object = jsonArray.getJSONObject(index);

                    PhoneDirectory directory = new PhoneDirectory();
                    directory.setSrno(object.getInt("srno"));
                    directory.setLocationName(object.getString("location"));
                    directory.setContactNumber(object.getString("contactno"));

                    directoryList.add(directory);
                }
                directoryAdapter.notifyDataSetChanged();
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
