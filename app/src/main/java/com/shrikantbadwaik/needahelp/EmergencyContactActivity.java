package com.shrikantbadwaik.needahelp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EmergencyContactActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_CONTACT_1 = 1;
    private static final int PICK_CONTACT_2 = 2;
    private static final int PICK_CONTACT_3 = 3;
    private static final int PICK_CONTACT_4 = 4;

    ImageButton buttonSearch1, buttonSearch2, buttonSearch3, buttonSearch4;
    EditText editContact1, editContact2, editContact3, editContact4, editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        buttonSearch1 = (ImageButton) findViewById(R.id.buttonSearch1);
        buttonSearch2 = (ImageButton) findViewById(R.id.buttonSearch2);
        buttonSearch3 = (ImageButton) findViewById(R.id.buttonSearch3);
        buttonSearch4 = (ImageButton) findViewById(R.id.buttonSearch4);

        editContact1 = (EditText) findViewById(R.id.editContact1);
        editContact2 = (EditText) findViewById(R.id.editContact2);
        editContact3 = (EditText) findViewById(R.id.editContact3);
        editContact4 = (EditText) findViewById(R.id.editContact4);
        editMessage = (EditText) findViewById(R.id.editMessage);

        buttonSearch1.setOnClickListener(this);
        buttonSearch2.setOnClickListener(this);
        buttonSearch3.setOnClickListener(this);
        buttonSearch4.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void saveMessage(View view) {
        if (editContact1.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty", Toast.LENGTH_SHORT).show();
        } else if (editContact2.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty", Toast.LENGTH_SHORT).show();
        } else if (editContact3.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty", Toast.LENGTH_SHORT).show();
        } else if (editContact4.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty", Toast.LENGTH_SHORT).show();
        } else if (editMessage.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty", Toast.LENGTH_SHORT).show();
        } else {
            //GPSTracker gpsTracker = new GPSTracker(this);

            MyService gps = new MyService(this);

            String message = editMessage.getText().toString();
            String locationMessage = message + " My Location is : " + "http://maps.google.com/?q=" + gps.getLatitude() + "," + gps.getLongitude();

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
            preferences1.edit().putString("Message", locationMessage).commit();

            Toast.makeText(this, "Message Saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HelpMeActivity.class));
            finish();
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

    @Override
    public void onClick(View view) {
        if (view == buttonSearch1) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT_1);
        } else if (view == buttonSearch2) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT_2);
        } else if (view == buttonSearch3) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT_3);
        } else if (view == buttonSearch4) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT_4);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String contactId, phoneNumber, hasPhone;
            Uri contactData = data.getData();
            Cursor contact = getContentResolver().query(contactData, null, null, null, null);

            if (requestCode == PICK_CONTACT_1) {
                if (resultCode == Activity.RESULT_OK) {

                    while (contact.moveToNext()) {
                        contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
                        hasPhone = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                editContact1.setText(phoneNumber);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                                preferences.edit()
                                        .putString("Number_1", editContact1.getText().toString())
                                        .commit();
                            }
                            phones.close();
                        }
                    }
                }
            } else if (requestCode == PICK_CONTACT_2) {
                if (resultCode == Activity.RESULT_OK) {
                    while (contact.moveToNext()) {
                        contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
                        hasPhone = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                editContact2.setText(phoneNumber);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                                preferences.edit()
                                        .putString("Number_2", editContact2.getText().toString())
                                        .commit();
                            }
                            phones.close();
                        }
                    }
                }
            } else if (requestCode == PICK_CONTACT_3) {
                if (resultCode == Activity.RESULT_OK) {
                    while (contact.moveToNext()) {
                        contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
                        hasPhone = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                editContact3.setText(phoneNumber);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                                preferences.edit()
                                        .putString("Number_3", editContact3.getText().toString())
                                        .commit();
                            }
                            phones.close();
                        }
                    }
                }
            } else if (requestCode == PICK_CONTACT_4) {
                if (resultCode == Activity.RESULT_OK) {
                    while (contact.moveToNext()) {
                        contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
                        hasPhone = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false";

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                editContact4.setText(phoneNumber);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                                preferences.edit()
                                        .putString("Number_4", editContact4.getText().toString())
                                        .commit();
                            }
                            phones.close();
                        }
                    }
                }
            }
        }
    }
}
