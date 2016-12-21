package com.shrikantbadwaik.needahelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;

public class ReportIncidentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_VIDEO = 200;
    private static final int REQUEST_CODE_AUDIO = 300;

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final int MEDIA_TYPE_AUDIO = 3;

    ImageView imageView;
    VideoView videoView;
    EditText editGetLongitude, editGetLatitude, editDescription, editContactDetail;
    Spinner categorySpinner;
    LinearLayout audioLayout, imgVidLayout;
    ArrayList<String> catrgoryList = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    ImageButton buttonCaptureImage, buttonCaptureVideo, buttonCaptureAudio;
    Bitmap bitmap;
    File imageFile, videoFile;
    CheckBox checkPolice, checkAmbulance, checkFireDept;
    ImageButton audioRecord, audioStop, audioPlay;
    String police, ambulance, fire;
    String outputAudioFile, audio_File;
    String image;

    private Uri imageFileUri, videoFileUri, audioFileUri, fileUri;
    private MediaRecorder myAudioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        imageView = (ImageView) findViewById(R.id.imageView);
        videoView = (VideoView) findViewById(R.id.videoView);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        buttonCaptureImage = (ImageButton) findViewById(R.id.buttonCaptureImage);
        buttonCaptureVideo = (ImageButton) findViewById(R.id.buttonCaptureVideo);
        buttonCaptureAudio = (ImageButton) findViewById(R.id.buttonCaptureAudio);

        editGetLongitude = (EditText) findViewById(R.id.editGetLongitude);
        editGetLatitude = (EditText) findViewById(R.id.editGetLatitude);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editContactDetail = (EditText) findViewById(R.id.editContactDetail);

        checkPolice = (CheckBox) findViewById(R.id.checkPolice);
        checkAmbulance = (CheckBox) findViewById(R.id.checkAmbulance);
        checkFireDept = (CheckBox) findViewById(R.id.checkFireDept);

        audioLayout = (LinearLayout) findViewById(R.id.audioLayout);
        audioRecord = (ImageButton) findViewById(R.id.audioRecord);
        audioStop = (ImageButton) findViewById(R.id.audioStop);
        audioPlay = (ImageButton) findViewById(R.id.audioPlay);

        imgVidLayout = (LinearLayout) findViewById(R.id.imgVidLayout);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        buttonCaptureImage.setOnClickListener(this);
        buttonCaptureVideo.setOnClickListener(this);
        buttonCaptureAudio.setOnClickListener(this);


        catrgoryList.add("Accident");
        catrgoryList.add("Murder");
        catrgoryList.add("Robbery");
        catrgoryList.add("Fire");
        catrgoryList.add("Rape");

        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, catrgoryList);
        categorySpinner.setAdapter(categoryAdapter);
    }

    public void getLocation(View view) {
        MyService gps = new MyService(this);

        if (gps.canGetLocation()) {
            double longitude = gps.getLongitude();
            editGetLongitude.setText("" + longitude);
            double latitude = gps.getLatitude();
            editGetLatitude.setText("" + latitude);
        } else {
            gps.showSettingsAlert();
        }

        /*GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.getLatitude());
            editGetLatitude.setText(stringLatitude);

            String stringLongitude = String.valueOf(gpsTracker.getLongitude());
            editGetLongitude.setText(stringLongitude);
        } else {
            gpsTracker.showSettingsAlert();
        }*/
    }

    public void submitReport(View view) {

        if (fileUri == imageFileUri) {
            fileUri = imageFileUri;
        } else if (fileUri == videoFileUri) {
            fileUri = videoFileUri;
        } else if (fileUri == audioFileUri) {
            fileUri = audioFileUri;
        }

        if (checkPolice.isChecked()) {
            police = checkPolice.getText().toString();
        } else {
            police = "NA";
        }

        if ((checkAmbulance.isChecked())) {
            ambulance = checkAmbulance.getText().toString();
        } else {
            ambulance = "NA";
        }

        if (checkFireDept.isChecked()) {
            fire = checkFireDept.getText().toString();
        } else {
            fire = "NA";
        }

        String urlString = "http://192.168.43.83:9090/NeedAHelp/SendReportServlet"
                + "?ReportName=" + categorySpinner.getSelectedItem().toString().replace(" ", "%20")
                + "&ReportDesc=" + editDescription.getText().toString().replace(" ", "%20")
                + "&Police=" + police.replace(" ", "%20")
                + "&Ambulance=" + ambulance.replace(" ", "%20")
                + "&Fire=" + fire.replace(" ", "%20")
                + "&NameOrPhoneno=" + editContactDetail.getText().toString().replace(" ", "%20")
                + "&Longitude=" + editGetLongitude.getText().toString().replace(" ", "%20")
                + "&Latitude=" + editGetLatitude.getText().toString().replace(" ", "%20")
                + "&ImageOrVideoOrAudio=" + fileUri.toString().replace(" ", "%20");

        //Log.d("ReportIncidentActivity", urlString);

        new ReportIncidentTask().execute(urlString);
    }

    class ReportIncidentTask extends AsyncTask<String, Void, InputStream> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ReportIncidentActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Please wait... Data is Uploading");
            dialog.show();
        }

        @Override
        protected InputStream doInBackground(String... urls) {
            String strUrl = urls[0];

            try {
                URL url = new URL(strUrl);

                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                return stream;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            dialog.dismiss();

            startActivity(new Intent(ReportIncidentActivity.this, HomePageActivity.class));
            finish();

            //MyService gps = new MyService(ReportIncidentActivity.this);
            //gps.stopUsingGPS();
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
        if (view == buttonCaptureImage) {
            try {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //imageFileUri = getOutputImageFileUri(MEDIA_TYPE_IMAGE);

                image = getOutputImageFile(MEDIA_TYPE_IMAGE);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                imageFileUri = Uri.parse(image);
                fileUri = imageFileUri;

                if (audioLayout.isShown()) {
                    audioLayout.setVisibility(View.GONE);
                    imgVidLayout.setVisibility(View.VISIBLE);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (view == buttonCaptureVideo) {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFileUri = getOutputVideoFileUri(MEDIA_TYPE_VIDEO);
            videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,60);
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);
            startActivityForResult(videoIntent, REQUEST_CODE_VIDEO);
            //videoFileUri=Uri.parse(video);
            fileUri = videoFileUri;

            if (audioLayout.isShown()) {
                audioLayout.setVisibility(View.GONE);
                imgVidLayout.setVisibility(View.VISIBLE);
            }
        } else if (view == buttonCaptureAudio) {
            if (imgVidLayout.isShown()) {
                imgVidLayout.setVisibility(View.GONE);
                audioLayout.setVisibility(View.VISIBLE);
            }
            getAudioController();
        }
    }

    private void getAudioController() {
        audioStop.setEnabled(false);
        audioPlay.setEnabled(false);

        try {
            outputAudioFile = getOutputAudioFile(MEDIA_TYPE_AUDIO);

            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputAudioFile);

            audioFileUri = Uri.parse(outputAudioFile);

            fileUri = audioFileUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        audioRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                audioRecord.setEnabled(false);
                audioStop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
            }
        });

        audioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                audioStop.setEnabled(false);
                audioPlay.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
            }
        });

        audioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputAudioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //private Uri getOutputImageFileUri(int type) throws FileNotFoundException {
    //  return Uri.fromFile(getOutputImageFile(type));
    //}

    private String getOutputImageFile(int type) throws FileNotFoundException {
        String img;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "App_Images");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        if (type == MEDIA_TYPE_IMAGE) {
            imageFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
            img = "" + imageFile;
        } else {
            return null;
        }
        //return imageFile;
        return img;
    }

    private Uri getOutputVideoFileUri(int type) {
        return Uri.fromFile(getOutputVideoFile(type));
    }

    private File getOutputVideoFile(int type) {
        //String vid;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "App_Videos");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        if (type == MEDIA_TYPE_VIDEO) {
            videoFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            //vid=""+videoFile;
        } else {
            return null;
        }

        return videoFile;
    }

    String getOutputAudioFile(int type) throws FileNotFoundException {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "App_Recording");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        if (type == MEDIA_TYPE_AUDIO) {
            File audioFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + timeStamp + ".mp3");
            audio_File = "" + audioFile;
        } else {
            return null;
        }
        return audio_File;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setView();

        if (data != null) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                try {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    FileOutputStream fileOutputStream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_VIDEO) {
                if (resultCode == RESULT_OK) {
                    videoView.setVideoURI(data.getData());
                }
            }
        }
    }

    private void setView() {
        if (fileUri == videoFileUri) {
            MediaController mediaController = new MediaController(this);
            videoView.setVideoURI(fileUri);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }
}
