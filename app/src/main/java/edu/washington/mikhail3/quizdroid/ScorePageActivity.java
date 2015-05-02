package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ScorePageActivity extends Activity {
    int totalQ;
    int score;
    int answered;
    int qNum;
    String quizName;
    int quizNumber;
    String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        TextView scores = (TextView) findViewById(R.id.scoreReport);
        Intent launchingIntent = getIntent();
        quizNumber = launchingIntent.getIntExtra("quiz", -1);
        quizName = launchingIntent.getStringExtra("quizName");
        quizID = launchingIntent.getStringExtra("quizNameID");
        totalQ = launchingIntent.getIntExtra("total", 0);
        qNum = launchingIntent.getIntExtra("question", 0);
        answered = launchingIntent.getIntExtra("answeredNum", 0);
        score = launchingIntent.getIntExtra("score", 0);
        int correctAns = launchingIntent.getIntExtra("correctNum", 0);
        int useAns = launchingIntent.getIntExtra("correctNum", 0);
        boolean correct = launchingIntent.getBooleanExtra("correct", false);
        TextView corrAnnounce = (TextView) findViewById(R.id.correct);
        if(correct){
            corrAnnounce.setText("CORRECT!");
            corrAnnounce.setTextColor(Color.parseColor("#093600"));
        } else {
            corrAnnounce.setText("INCORRECT");
            corrAnnounce.setTextColor(Color.parseColor("#990000"));
        }
        String useAnsCode = quizID + qNum + "_a_"+(useAns+1);
        String corrAnsCode = quizID + qNum + "_a_"+(correctAns+1);
        TextView userAnsShow = (TextView) findViewById(R.id.userAns);
        TextView corrAnsShow = (TextView) findViewById(R.id.corrAns);
        userAnsShow.setText(getResources().getString(getResources().getIdentifier(useAnsCode, "string", getPackageName())));;
        corrAnsShow.setText(getResources().getString(getResources().getIdentifier(corrAnsCode, "string", getPackageName())));;
        TextView scoreReport = (TextView) findViewById(R.id.scoreReport);
        scoreReport.setText("Answered " + score+" correctly out of " + totalQ);
        Button button = (Button) findViewById(R.id.next);
        if(qNum < totalQ){
            button.setText("Next Question");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent next = new Intent(ScorePageActivity.this, question.class);
                    next.putExtra("quiz", quizNumber);
                    next.putExtra("quizName", quizName);
                    next.putExtra("question", qNum++);
                    next.putExtra("total", totalQ);
                    next.putExtra("answeredNum", answered);
                    next.putExtra("score", score);
                    next.putExtra("quizNameID", quizID);
                    next.putExtra("answered", answered);
                    Log.i("MainActivity", "Firing intent" + next);
                    startActivity(next);
                    finish();
                }
            });
        } else {
            button.setText("Finish");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent next = new Intent(ScorePageActivity.this, MainActivity.class);
                    startActivity(next);
                    finish();
                }
            });
        }
    }


}
