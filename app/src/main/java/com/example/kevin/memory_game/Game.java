package com.example.kevin.memory_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class Game extends AppCompatActivity {

    //private Boolean startNew;
    private int clickCounter;
    private int picID1;
    private int picID2;
    private int points = 0;
    ArrayList<ImageButton> list;
    ArrayList<Integer> remainingImages;
    Integer[] listOfImages;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        list = new ArrayList<>();
        remainingImages = new ArrayList<>();

        listOfImages= new Integer[]{R.drawable.curry, R.drawable.draymond, R.drawable.ellis, R.drawable.iverson, R.drawable.jordan,
                R.drawable.kobe, R.drawable.lebron, R.drawable.magic, R.drawable.nash, R.drawable.yao};


        remainingImages.addAll(Arrays.asList(listOfImages));

        for(int i = 1; i < 21; i++){
            String imageName = "imageButton" + i;
            int imageId = this.getResources().getIdentifier(imageName, "id", getPackageName());
            ImageButton origImage = (ImageButton) findViewById(imageId);

            list.add((ImageButton) findViewById(imageId));
        }


        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //startNew = savedInstanceState.getBoolean("startNew");
        //if(startNew == null)
        initializePictures();
        clickCounter = 0;
        checkVisibility();
        displayPoints();


        Log.d("lifecycle", "onCreate");



    }



    /*
    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        //state.putInt("points", points);
        //state.putAll(persistant);

        //Intent intent = new Intent(this, Game.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
        //state.putBoolean("startNew", false);

        Log.d("lifecycle", "onSaveInstanceState");

        //Log.d("startNew", startNew + "");
    }

    @Override
    public void onRestoreInstanceState(Bundle state){
        super.onRestoreInstanceState(state);
        Log.d("lifecycle", "onRestoreInstanceState");

     //   startNew = state.getBoolean("startNew");
     //   Log.d("startNew", startNew + "");
        //intent = new Intent(this, Game.class);
        //startActivity(intent);
        //points = state.getInt("points");
        //Toast.makeText(this, state.getInt("points") + "", Toast.LENGTH_LONG).show();
        //displayPoints();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.Restart:
                restart();
                return true;
            case R.id.Shuffle:
                reOrganize();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("lifecycle", "onStart");

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("lifecycle", "onResume");
    }
    */

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("lifecycle", "onPause");


        saveVisibilityAndPoints();
    }

    /*
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("lifecycle", "onStop");;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("lifecycle", "onDestroy");;

    }

    */

    public void initializePictures(){
        Random rand = new Random();
        for(int i = 0; i < listOfImages.length; i++){

            int num = rand.nextInt(list.size());
            int counter = 0;
            while(counter < 2){
                if(list.get(num).getTag() == null){
                    list.get(num).setTag(listOfImages[i]);

                    counter++;
                }
                num = rand.nextInt(list.size());
            }
        }
        Log.d("lifecycle", "initializePictures");
    }


    @OnClick({R.id.imageButton1,R.id.imageButton2,R.id.imageButton3,R.id.imageButton4,R.id.imageButton5,R.id.imageButton6,
            R.id.imageButton7,R.id.imageButton8,R.id.imageButton9,R.id.imageButton10,R.id.imageButton11,R.id.imageButton12,
            R.id.imageButton13,R.id.imageButton14,R.id.imageButton15,R.id.imageButton16,R.id.imageButton17,R.id.imageButton18,
            R.id.imageButton19,R.id.imageButton20})
    public void flipImage(View view){

        clickCounter++;
        ImageButton image = (ImageButton) view;

        YoYo.with(Techniques.FlipInX)
                .duration(1000)
                .playOn(image);

        if(clickCounter == 1){
            picID1 = view.getId();
        }
        else{
            picID2 = view.getId();
        }

        image.setImageResource((int)image.getTag());
        image.setEnabled(false);

        if(clickCounter == 2){
            checkPictures();
        }

    }

    public void checkPictures(){

        for(int i = 0; i < list.size(); i++){
            list.get(i).setEnabled(false);
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                //finish();
                //startActivity(getIntent());
                ImageButton image1 = (ImageButton) findViewById(picID1);
                ImageButton image2 = (ImageButton) findViewById(picID2);


                if(image1.getTag().equals(image2.getTag()) ){

                    image1.setVisibility(View.INVISIBLE);
                    image2.setVisibility(View.INVISIBLE);

                    points++;
                    displayPoints();

                    //removes found pictures from remainingImages
                    for(int i = 0; i < remainingImages.size(); i++){
                        if(remainingImages.get(i) == image1.getTag()){
                            remainingImages.remove(image1.getTag());
                        }
                    }

                }
                else {

                    image1.setImageResource(R.drawable.basketball);
                    image2.setImageResource(R.drawable.basketball);
                }

                for(int i = 0; i < list.size(); i++){
                    list.get(i).setEnabled(true);
                }

            }
        }, 1000);
        clickCounter = 0;
    }

    public void displayPoints(){
        TextView pointsText = (TextView) findViewById(R.id.points);
        pointsText.setText("Points: " + points );
    }

    public void checkVisibility(){

        points = prefs.getInt("points", 0);

        //check for visibility
        for(int i = 0; i < list.size();i++){
            String prefID = String.valueOf(list.get(i).getId());
            if(prefs.getInt(prefID, View.VISIBLE) == View.VISIBLE){
                list.get(i).setVisibility(View.VISIBLE);
            }
            else{
                list.get(i).setVisibility(View.INVISIBLE);
            }
        }

    }

    public void saveVisibilityAndPoints(){

        prefsEditor.putInt("points", points);


        //save visibility
        for(int i = 0; i < list.size(); i++){
            String prefName = String.valueOf(list.get(i).getId());
            prefsEditor.putInt(prefName, list.get(i).getVisibility());
        }

        prefsEditor.apply();




    }

    public void restart(){

        //reset remaining images
        remainingImages.clear();
        remainingImages.addAll(Arrays.asList(listOfImages));

        //reset points
        prefsEditor.clear();
        prefsEditor.apply();


        //reset the tags and pictures back to basketball for all imageButtons
        for(int i = 0; i < list.size();i++){
            list.get(i).setTag(null);
            list.get(i).setImageResource(R.drawable.basketball);
        }

        //
        initializePictures();
        clickCounter = 0;
        checkVisibility();
        displayPoints();


    }

    //@OnClick({R.id.clear})
    public void reOrganize(){


        //meant to clear the tags (but also clear points)
        prefsEditor.clear();
        prefsEditor.apply();


        Random rand = new Random();
        int remainLength = (remainingImages.size()*2);

        //reset images to basketball
        for(int i = 0; i < list.size();i++){
            list.get(i).setTag(null);
            list.get(i).setImageResource(R.drawable.basketball);
        }

        //rearrange remaining images
        for(int i = 0; i < remainingImages.size(); i++){
            int num = rand.nextInt(remainLength);
            int counter = 0;
            while(counter < 2){
                if(list.get(num).getTag() == null){
                    list.get(num).setTag(remainingImages.get(i));

                    counter++;
                }
                num = rand.nextInt(remainLength);
            }
        }

        //leave the found images invisible
        for(int i = remainLength; i < list.size(); i++){
            String prefName = String.valueOf(list.get(i).getId());
            prefsEditor.putInt(prefName, View.INVISIBLE);
        }

        //save the points
        prefsEditor.putInt("points", points);
        prefsEditor.apply();

        //display new state
        checkVisibility();
        displayPoints();
    }


}


