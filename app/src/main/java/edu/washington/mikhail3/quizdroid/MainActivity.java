package edu.washington.mikhail3.quizdroid;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {
    ArrayList<Topic> topics;

    QuizApp quizApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizApp = (QuizApp) getApplication();
        topics = quizApp.getQuiz();
        final ArrayList<String> quizzes = new ArrayList<String>();
        int length = topics.size();

        for(int i = 0; i < length; i++){
            Topic topic = topics.get(i);
            quizzes.add(topic.getTitle());
        }

        ListView quizList = (ListView)findViewById(R.id.quizList);
        quizList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_activated_1, quizzes));
        quizList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Go bring some other activity around the item selected
                //if (v.getId() == R.id.p1m5){
                Intent next = new Intent(MainActivity.this, QuizActivity.class);
                next.putExtra("quiz", position);
                next.putExtra("quizData", topics);
                startActivity(next);
                finish();
            }
        });
    }
}



