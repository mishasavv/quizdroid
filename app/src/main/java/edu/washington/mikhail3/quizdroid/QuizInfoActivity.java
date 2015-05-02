package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import static java.lang.Integer.parseInt;


public class QuizInfoActivity extends Activity {
    int[] questions = {3,3,3};
    String[] quizzes = {"math", "physics", "superhero"};
    int questionNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_info);
        Intent launchingIntent = getIntent();
        final int quizNumber = launchingIntent.getIntExtra("quiz", -1);
        int q = launchingIntent.getIntExtra("question", -1);
        if(q != -1){
            questionNumber = q;
        } else {
            questionNumber = 1;
        }
        final String quizName = launchingIntent.getStringExtra("quizName");
        TextView descr = (TextView) findViewById(R.id.quizDescr);
        TextView title = (TextView) findViewById(R.id.title);
        TextView qNum = (TextView) findViewById(R.id.qNumber);
        qNum.setText("This quiz has "+questions[quizNumber]+" questions. Enjoy!");
        String address = quizzes[quizNumber];
        String id = quizzes[quizNumber]+"_quiz_info";
        descr.setText(getResources().getString(getResources().getIdentifier(id, "string", getPackageName())));
        title.setText(quizName);
        Button button= (Button) findViewById(R.id.startQuiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(QuizInfoActivity.this, question.class);
                next.putExtra("quiz", quizNumber);
                next.putExtra("quizName", quizName);
                next.putExtra("question", questionNumber);
                next.putExtra("total", questions[quizNumber]);
                next.putExtra("answeredNum", 0);
                next.putExtra("score", 0);
                next.putExtra("quizNameID", quizzes[quizNumber]);
                Log.i("MainActivity", "Firing intent" + next);
                startActivity(next);
                finish();
            }
        });

    }
}
