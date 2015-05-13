package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class Question extends android.support.v4.app.Fragment {
    int score;
    int answered;
    int qNum;
    int quizNumber;
    int ansNumber;
    int correctNum;
    boolean correct;
    ArrayList<Topic> topics;
    ArrayList<Quiz> questions;


    public Question() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        final Intent launchingIntent = getActivity().getIntent();
        quizNumber = launchingIntent.getIntExtra("quiz", -1);
        qNum = launchingIntent.getIntExtra("question", 0);
        topics = (ArrayList <Topic>) launchingIntent.getSerializableExtra("quizData");
        questions = topics.get(quizNumber).getQuestions();
        TextView title = (TextView) rootView.findViewById(R.id.quizTitle);
        title.setText(topics.get(quizNumber).getTitle());
        answered = launchingIntent.getIntExtra("answered", 0);
        answered++;
        score = launchingIntent.getIntExtra("score", 0);
        TextView text = (TextView) rootView.findViewById(R.id.questionText);
        RadioButton ans1 = (RadioButton) rootView.findViewById(R.id.ans1);
        RadioButton ans2 = (RadioButton) rootView.findViewById(R.id.ans2);
        RadioButton ans3 = (RadioButton) rootView.findViewById(R.id.ans3);
        RadioButton ans4 = (RadioButton) rootView.findViewById(R.id.ans4);
        text.setText(questions.get(qNum-1).getQuestion());
        ans1.setText(questions.get(qNum-1).getAns1());
        ans2.setText(questions.get(qNum-1).getAns2());
        ans3.setText(questions.get(qNum-1).getAns3());
        ans4.setText(questions.get(qNum-1).getAns4());
        final RadioGroup answers = (RadioGroup) rootView.findViewById(R.id.ansGroup);
        Button button = (Button) rootView.findViewById(R.id.submitAns);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answers.getCheckedRadioButtonId()!=-1){
                    int id= answers.getCheckedRadioButtonId();
                    View radioButton = answers.findViewById(id);
                    int radioId = answers.indexOfChild(radioButton);
                    ansNumber = radioId;
                    correctNum = questions.get(qNum-1).getCorrect();
                    if(correctNum==radioId){
                        score++;
                        correct = true;
                    }
                    }
                    qNum++;
                    //next.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("score", score);
                    launchingIntent.putExtra("question", qNum);
                    launchingIntent.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("ansNumber", ansNumber);
                    launchingIntent.putExtra("correctNum", correctNum);
                    launchingIntent.putExtra("correct", correct);
                    launchingIntent.putExtra("answered", answered);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container, new ScorePage())
                        .commit();
            }
        });
        return rootView;
    }
}
