package com.example.mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Songsfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listView;

    private String mParam1;
    private String mParam2;

    private DatabaseHelper databaseHelper;

    public Songsfragment() {
        // Required empty public constructor
    }

    public static Songsfragment newInstance(String param1, String param2) {
        Songsfragment fragment = new Songsfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        String COLUMN_TITLE = "songname";
        String TABLE_SONGS = "songstable";

        listView = rootView.findViewById(R.id.listview);

        // Open the database for reading
        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db1 = databaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve (in this case, only the title)
        String[] projection = {COLUMN_TITLE};

        // Execute the query
        Cursor cursor = db1.query(TABLE_SONGS, projection, null, null, null, null, null);

        List<String> songTitles = new ArrayList<>();

        // Iterate through the cursor to retrieve the song titles
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve the title value from the cursor
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));

                // Add the song title to the list
                songTitles.add(title);
            } while (cursor.moveToNext());
        }

        // Create an instance of the custom adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, songTitles);
        // Set the adapter to the ListView
        listView.setAdapter(adapter);


        //to set onclick listener on view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTitle = (String) parent.getItemAtPosition(position);

                Intent it=new Intent(requireContext(),PlaySelectedActivity.class);

                //putng all required datas in intent
                it.putExtra("selectedTitle",selectedTitle);
                startActivity(it);

            }
        });

        return rootView;
    }
}
