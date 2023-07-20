package com.example.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    String title;
    String text;
    String url;
    private Button start,stop;
    MediaPlayer mediaPlayer;
    // Initialize the progress UI components
    ProgressBar progressBar ;
    TextView progressTextView ;
    boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        progressTextView = findViewById(R.id.progressTextView);
        progressBar = findViewById(R.id.progressBar);


        Intent it=getIntent();

        url=it.getStringExtra("songurl");

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url);
        title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
      //  Toast.makeText(this, title, Toast.LENGTH_SHORT).show();




//        Button Start = findViewById(R.id.start);
//        Button stop=findViewById(R.id.stop)
        if((isStringPresentInTable(title))==true){

           Toast.makeText(this,"Already Downloaded See Your list",Toast.LENGTH_SHORT).show();

        }
        else if((isStringPresentInTable(title))==false){
            downloadAudio(url);
            saveindb();
        }

        start=findViewById(R.id.st);
        stop=findViewById(R.id.st1);


        // Declare a global variable

// Inside onCreate() method
// ...

        stop.setEnabled(false);
        start.setEnabled(false);



// Inside the start.setOnClickListener() method
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();

                File fileDir = getFilesDir();
                File audioFile = new File(fileDir, title);
                // Create and prepare the MediaPlayer

                try {
                    mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isPlaying ==false) {

                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_SHORT).show();
                    start.setEnabled(false);
                    isPlaying = true; // Set isPlaying to true when the song starts
                }
            }
        });

// Inside the stop.setOnClickListener() method
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying==true) {
                    mediaPlayer.pause();
                    stop.setText("RESUME");
                    start.setEnabled(true);
                    isPlaying = false;
                    Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
                }else{
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    mediaPlayer.start();
                    stop.setText("STOP");
                    start.setEnabled(false);
                    isPlaying = true;
                }

                // Set isPlaying to false when the song is stopped
            }
        });

    }


    private void downloadAudio(String audioUrl) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    
                    URL url = new URL(audioUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Set up the file output stream
                    File fileDir = getFilesDir();
                    File audioFile = new File(fileDir, title);
                    FileOutputStream outputStream = new FileOutputStream(audioFile);

                    // Get the content length for calculating progress
                    int fileLength = connection.getContentLength();
                    int totalBytesRead = 0;
                    int bytesRead;

                    // Start the download
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024];

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Calculate progress percentage
                        final int progress = (int) ((totalBytesRead * 100L) / fileLength);

                        // Update progress UI on the main UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update the ProgressBar and TextView with the progress
                                progressBar.setProgress(progress);

                                text = "The Song file is getting downloaded " + progress + "%";
                                progressTextView.setText(text);
                                if (progress == 100) {
                                    stop.setEnabled(true);
                                    start.setEnabled(true);
                                }
                            }
                        });
                    }

                    // Clean up the streams
                    outputStream.close();
                    inputStream.close();

                    // Download completed
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), title + " downloaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error occurred during audio download", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }



    public void saveindb(){

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Retrieve the song title
        String songTitle = title;
        String COLUMN_TITLE="songname";
        String TABLE_SONGS="songstable";

        // Open the database for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to hold the values to be inserted
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, songTitle);

        // Insert the song title into the table
        long newRowId = db.insert(TABLE_SONGS, null, values);

        // Close the database connection
        db.close();
        //Toast.makeText(this, "Title saved in database", Toast.LENGTH_SHORT).show();
    }
    public boolean isStringPresentInTable(String searchString) {
        SQLiteDatabase database = null;
        Cursor cursor = null;

        String databasePath = getDatabasePath("SongsDatabase.db").getPath();

        try {
            database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);

            String tableName = "songstable";
            String columnName = "songname";

            String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
            String[] selectionArgs = {searchString};

            cursor = database.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
    }
    // Override the onBackPressed() method
    @Override
    public void onBackPressed() {

            mediaPlayer.stop();
            mediaPlayer.release();
            isPlaying = false;

        super.onBackPressed();
    }

}