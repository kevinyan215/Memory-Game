package com.example.kevin.memory_game;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Log.d("lifecycle", "MainActivity onCreate");
    }

    @OnClick(R.id.playGame)
    public void playGame(View view) {
        //intent call Game
        Intent playGameIntent = new Intent(this,Game.class);
        startActivity(playGameIntent);
    }

    @OnClick(R.id.rules)
    public void rulesOnClick(View view){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Rules_Fragment frag = (Rules_Fragment) manager.findFragmentByTag("frag");

        if(frag == null){
            frag = new Rules_Fragment();
            transaction.add(R.id.layout, frag, "frag");
        }
        else{
            transaction.remove(frag);
        }
        Log.d("frag", frag + "");
        transaction.commit();



    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.d("lifecycle", "MainActivity onDestroy");

        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();

    }
}
