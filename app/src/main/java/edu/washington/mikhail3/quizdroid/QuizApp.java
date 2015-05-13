package edu.washington.mikhail3.quizdroid;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.app.Application;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
public class QuizApp extends Application{
    private static QuizApp instance;
    ArrayList<Topic> topics;
    SingleApp singleApp;
    ArrayList<Quiz> questions;
    public QuizApp() {
        if (instance == null) {
            SingleApp.initInstance();
        } else {
            Log.e("QuizApp", "Error. You tried to create more than 1 QuizApp. Bad boy.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this.getApplicationContext();
        try {
            InputStream inputStream = getAssets().open("data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        singleApp = new SingleApp();
        topics = singleApp.getQuiz(context);

    }
    public ArrayList<Topic> getQuiz(){
        return topics;
    }
    private void onButtonClick(){

    }

}
