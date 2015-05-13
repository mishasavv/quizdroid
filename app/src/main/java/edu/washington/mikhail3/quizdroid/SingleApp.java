package edu.washington.mikhail3.quizdroid;

import android.content.Context;
import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;
import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import android.app.*;
import android.widget.Toast;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
public class SingleApp implements TopicRepository {
    private static SingleApp instance;
   public ArrayList<Topic> topics;

    //public Context c;

    public static SingleApp getInstance() {
        return instance;
    }

    public static void initInstance() {
        if (instance == null) {
            instance = new SingleApp();
        }
    }

//    public ArrayList<Topic> getQuiz() {
//
//        try {
//            Context context = new Context();
//
//            InputStream in = SingleApp.context.getAssets().open("data.json");
//            topics = readJsonStream(in);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return topics;
//    }
    public ArrayList<Topic> getQuiz(Context c) {
    ArrayList<Topic> topics = new ArrayList<Topic>();
        //this.c = c;
    try {
        topics = readJsonStream(c);
    } catch (IOException e) {
        e.printStackTrace();
    }
        return topics;
    }
    private ArrayList<Topic> readJsonStream(Context c) throws IOException {
        InputStream in = c.getAssets().open("data.json");
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Toast.makeText(c, "hi3", Toast.LENGTH_LONG).show();
        try {
            Toast.makeText(c, "hi", Toast.LENGTH_LONG).show();
            return readTopicArray(reader);

        } finally {
            Toast.makeText(c, "hi2", Toast.LENGTH_LONG).show();
            reader.close();
        }
    }
    private ArrayList<Topic> readTopicArray(JsonReader reader) throws IOException {

        ArrayList<Topic> topics = new ArrayList<Topic>();
        reader.beginArray();
        while (reader.hasNext()) {
            topics.add(readTopic(reader));
        }
        reader.endArray();
        return topics;
    }

    private Topic readTopic(JsonReader reader) throws IOException {
        Topic t = new Topic();
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                t.setTitle(reader.nextString());
            } else if (name.equals("desc")) {
                t.setDesc(reader.nextString());
            } else if (name.equals("questions")) {
                ArrayList<Quiz> qs = new ArrayList<Quiz>();
                reader.beginArray();
                while (reader.hasNext()) {
                    Quiz q = readQs(reader);
                    qs.add(q);
                }
                reader.endArray();
                t.setQuestions(qs);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return t;
    }
    private Quiz readQs(JsonReader reader) throws IOException {
        Quiz q = new Quiz();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("text")) {
                q.setQuestion(reader.nextString());
            } else if (name.equals("answer")) {
                q.setCorrect(reader.nextInt());
            } else if (name.equals("answers") && reader.peek() != JsonToken.NULL) {
                String[] ans = readStringArray(reader);
                q.setAns1(ans[0]);
                q.setAns2(ans[1]);
                q.setAns3(ans[2]);
                q.setAns4(ans[3]);
            } else {
            reader.skipValue();
            }
        }
        return q;
    }
    private String[] readStringArray(JsonReader reader) throws IOException {
        String[] strings = new String[4];
        reader.beginArray();
        int i = 0;
        while (reader.hasNext() && i < 4) {
            strings[i] = reader.nextString();
            i++;
        }
        reader.endArray();
        return strings;
     }






}

