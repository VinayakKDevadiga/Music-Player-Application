package com.example.mymusicplayer;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class Donwloadfragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String songurl;

    public static Donwloadfragment newInstance(String param1, String param2) {
        Donwloadfragment fragment = new Donwloadfragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);

        // Retrieve the button from the inflated layout
        Button downloadSongButton = rootView.findViewById(R.id.startdownload);

        // To get the URL from EditText
        EditText ed = rootView.findViewById(R.id.url);

        downloadSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the URL from the EditText when the button is clicked
                songurl = ed.getText().toString();


                // Check if network connectivity is available
                ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                boolean isNetworkAvailable = networkInfo != null && networkInfo.isConnected();


                if (!isNetworkAvailable) {
                    // Internet is not available
                    Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT).show();

                }
                else if (songurl.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter the URL", Toast.LENGTH_SHORT).show();
                }
                else if(isValidUrl(songurl)) {
                        // Perform actions when the button is clicked
                        Intent intent = new Intent(requireContext(), DownloadActivity.class);
                        intent.putExtra("songurl", songurl);
                        startActivity(intent);
                }
                else{
                        Toast.makeText(requireContext(), "NOT a Valid Link", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        // Return inflated view
        return rootView;
    }

    public boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
