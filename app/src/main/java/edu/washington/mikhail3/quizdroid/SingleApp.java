package edu.washington.mikhail3.quizdroid;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.ArrayList;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
public class SingleApp implements TopicRepository {
    private static SingleApp instance;
    public final static String FILE = "questions.json";

    public static SingleApp getInstance() {
        return instance;
    }

    private ArrayList<Topic> topics;


    public SingleApp(Context c) {
        topics = new ArrayList<Topic>();
        makeTopics(c);
    }

    private void makeTopics(Context c) {
        try {
            JSONArray topicsArr = new JSONArray(loadJSON(c));
            for (int i = 0; i < topicsArr.length(); i++) {
                JSONObject topicObj = topicsArr.getJSONObject(i);
                JSONArray questionsArr = topicObj.getJSONArray("questions");
                Topic topic = new Topic();
                topic.setDesc(topicObj.getString("desc"));
                topic.setTitle(topicObj.getString("title"));
                Log.v("SA", " " + questionsArr.length());
                for (int j = 0; j < questionsArr.length(); j++) {
                    JSONObject questionObj = questionsArr.getJSONObject(j);
                    JSONArray answersArr = questionObj.getJSONArray("answers");
                    Quiz question = new Quiz();
                    question.setQuestion(questionObj.getString("text"));
                    question.setCorrect(Integer.parseInt(questionObj.getString("answer")));
                    question.setAns1(answersArr.get(0).toString());
                    question.setAns2(answersArr.get(1).toString());
                    question.setAns3(answersArr.get(2).toString());
                    question.setAns4(answersArr.get(3).toString());
                    topic.addQ(question);
                }
                topics.add(topic);
            }
        } catch (JSONException e) {
            Log.e("SingleApp", "JSON Exception!", e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static String loadJSON(Context c) {
        String json;
        try {
            InputStream in = c.getAssets().open(FILE);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadJSON(FileInputStream in) {
        String json;
        try {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
    public static void write(String data, Context c) {
        try {
            File file = new File(c.getFilesDir().getAbsolutePath(), FILE);
            FileOutputStream out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.close();
        }
        catch (IOException e) {
            Log.e("Failed to write", "Exception " + e.toString());
        }
    }


    public ArrayList<Topic> getQuiz() {
        return topics;
    }
}
