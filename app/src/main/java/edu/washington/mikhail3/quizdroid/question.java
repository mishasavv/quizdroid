package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class question extends Activity {
    int totalQ;
    int score;
    int answered;
    int qNum;
    int[] mathKey = {0, 1, 0};
    int[] physicsKey = {2, 3, 2};
    int[] heroesKey = {1, 2, 3};
    String quizName;
    int quizNumber;
    String quizId;
    int ansNumber;
    int correctNum;
    boolean correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent launchingIntent = getIntent();
        quizNumber = launchingIntent.getIntExtra("quiz", -1);
        quizName = launchingIntent.getStringExtra("quizName");
        quizId = launchingIntent.getStringExtra("quizNameID");
        totalQ = launchingIntent.getIntExtra("total", 0);
        qNum = launchingIntent.getIntExtra("question", 0);
        answered = launchingIntent.getIntExtra("answeredNum", 0);
        answered++;
        score = launchingIntent.getIntExtra("score", 0);
        TextView text = (TextView) findViewById(R.id.questionText);
        RadioButton ans1 = (RadioButton) findViewById(R.id.ans1);
        RadioButton ans2 = (RadioButton) findViewById(R.id.ans2);
        RadioButton ans3 = (RadioButton) findViewById(R.id.ans3);
        RadioButton ans4 = (RadioButton) findViewById(R.id.ans4);
        String qId = quizId+"_q_"+qNum;
        String a1Id = quizId+qNum+"_a_1";
        String a2Id = quizId+qNum+"_a_2";
        String a3Id = quizId+qNum+"_a_3";
        String a4Id = quizId+qNum+"_a_4";
        text.setText(getResources().getString(getResources().getIdentifier(qId, "string", getPackageName())));
        ans1.setText(getResources().getString(getResources().getIdentifier(a1Id, "string", getPackageName())));
        ans2.setText(getResources().getString(getResources().getIdentifier(a2Id, "string", getPackageName())));
        ans3.setText(getResources().getString(getResources().getIdentifier(a3Id, "string", getPackageName())));
        ans4.setText(getResources().getString(getResources().getIdentifier(a4Id, "string", getPackageName())));
        final RadioGroup answers = (RadioGroup) findViewById(R.id.ansGroup);
        Button button = (Button) findViewById(R.id.submitAns);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answers.getCheckedRadioButtonId()!=-1){
                    int id= answers.getCheckedRadioButtonId();
                    View radioButton = answers.findViewById(id);
                    int radioId = answers.indexOfChild(radioButton);
                    ansNumber = radioId;
                    if(quizNumber == 0){
                        correctNum = mathKey[qNum-1];
                        if(mathKey[qNum-1]==radioId){
                            score++;
                            correct = true;
                        }
                    } else if(quizNumber == 1){
                        correctNum = physicsKey[qNum-1];
                        if(physicsKey[qNum-1]==radioId){
                            score++;
                            correct = true;
                        }
                    } else if(quizNumber == 2) {
                        correctNum = heroesKey[qNum-1];
                        if (heroesKey[qNum-1] == radioId) {
                            score++;
                            correct = true;
                        } else {
                            correct = false;
                        }

                    }
                    qNum++;
                    Intent next = new Intent(question.this, ScorePageActivity.class);
                    //next.putExtra("quiz", quizNumber);
                    next.putExtra("quizName", quizName);
                    next.putExtra("score", score);
                    next.putExtra("question", qNum);
                    next.putExtra("quiz", quizNumber);
                    next.putExtra("question", 1);
                    next.putExtra("total", totalQ);
                    next.putExtra("quizNameID", quizId);
                    next.putExtra("ansNumber", ansNumber);
                    next.putExtra("correctNum", correctNum);
                    next.putExtra("correct", correct);
                    //next.putExtra("total", questions[quizNumber]);
                    //next.putExtra("quizNameID", quizzes[quizNumber]);
                    Log.i("MainActivity", "Firing intent" + next);
                    startActivity(next);
                    finish();
                }
            }
        });


        }
        public void nextQuestion(String quizId){
            if(qNum <= totalQ){
                TextView text = (TextView) findViewById(R.id.questionText);
                RadioButton ans1 = (RadioButton) findViewById(R.id.ans1);
                RadioButton ans2 = (RadioButton) findViewById(R.id.ans2);
                RadioButton ans3 = (RadioButton) findViewById(R.id.ans3);
                RadioButton ans4 = (RadioButton) findViewById(R.id.ans4);
                String qId = quizId+"_q_"+qNum;
                String a1Id = quizId+qNum+"_a_1";
                String a2Id = quizId+qNum+"_a_2";
                String a3Id = quizId+qNum+"_a_3";
                String a4Id = quizId+qNum+"_a_4";
                text.setText(getResources().getString(getResources().getIdentifier(qId, "string", getPackageName())));
                ans1.setText(getResources().getString(getResources().getIdentifier(a1Id, "string", getPackageName())));
                ans2.setText(getResources().getString(getResources().getIdentifier(a2Id, "string", getPackageName())));
                ans3.setText(getResources().getString(getResources().getIdentifier(a3Id, "string", getPackageName())));
                ans4.setText(getResources().getString(getResources().getIdentifier(a4Id, "string", getPackageName())));

            } else{
                Intent next = new Intent(question.this, ScorePageActivity.class);
                //next.putExtra("quiz", quizNumber);
                next.putExtra("quizName", quizName);
                next.putExtra("score", score);
                next.putExtra("question", qNum);
                next.putExtra("quiz", quizNumber);
                next.putExtra("total", totalQ);
                next.putExtra("quizNameID", quizId);
                next.putExtra("ansNumber", ansNumber);
                next.putExtra("correctNum", correctNum);
                next.putExtra("correct", correct);
                next.putExtra("answered", answered);

                //next.putExtra("total", questions[quizNumber]);
                //next.putExtra("quizNameID", quizzes[quizNumber]);
                Log.i("MainActivity", "Firing intent" + next);
                startActivity(next);
                finish();
            }
        }
    }


