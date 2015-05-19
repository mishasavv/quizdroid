package edu.washington.mikhail3.quizdroid;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.app.Application;
import android.view.View;
import android.widget.Toast;

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
            SingleApp.getInstance();
        } else {
            Log.e("QuizApp", "Error. You tried to create more than 1 QuizApp. Bad boy.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this.getApplicationContext();

        singleApp = new SingleApp(context);
        topics = singleApp.getQuiz();


    }
    public ArrayList<Topic> getQuiz(){
        return topics;
    }
    private void onButtonClick(){

    }

}
