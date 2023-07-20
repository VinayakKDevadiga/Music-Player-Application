package com.example.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button playbtn,download;
    static int x=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playbtn=findViewById(R.id.play);
        download=findViewById(R.id.download);

        FragmentManager fmanager=getSupportFragmentManager();

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x==0 |x==2 ){
                    fmanager.beginTransaction()
                            .replace(R.id.fragmentcontainer, Songsfragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    x=1;
                }
                else{
                    x=0;
                    fmanager.beginTransaction()
                            .replace(R.id.fragmentcontainer, BlankFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x==1 | x==0) {
                    fmanager.beginTransaction()
                            .replace(R.id.fragmentcontainer, Donwloadfragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    x=2;
                }
                else{
                    x=0;
                    fmanager.beginTransaction()
                            .replace(R.id.fragmentcontainer, BlankFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                }
            }
        });


    }
}