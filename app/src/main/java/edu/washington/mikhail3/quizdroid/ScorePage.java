package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class ScorePage extends android.support.v4.app.Fragment {
    int score;
    int answered;
    int qNum;
    int quizNumber;
    int ansNumber;
    int correctNum;
    boolean correct;
    int correctAns;
    int useAns;
    ArrayList<Topic> topics;
    ArrayList<Quiz> questions;

    public ScorePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score_page, container, false);

        TextView scores = (TextView) rootView.findViewById(R.id.scoreReport);
        final Intent launchingIntent = getActivity().getIntent();
        quizNumber = launchingIntent.getIntExtra("quiz", -1);
        qNum = launchingIntent.getIntExtra("question", 0);
        answered = launchingIntent.getIntExtra("answered", 0);
        score = launchingIntent.getIntExtra("score", 0);
        correctAns = launchingIntent.getIntExtra("correctNum", 0) + 1;
        useAns = launchingIntent.getIntExtra("ansNumber", 0) + 1;
        boolean correct = launchingIntent.getBooleanExtra("correct", false);
        TextView title = (TextView) rootView.findViewById(R.id.quizTitle1);
        title.setText(topics.get(quizNumber).getTitle());
        TextView corrAnnounce = (TextView) rootView.findViewById(R.id.correct);
        if(correct){
            corrAnnounce.setText("CORRECT!");
            corrAnnounce.setTextColor(Color.parseColor("#093600"));
        } else {
            corrAnnounce.setText("INCORRECT");
            corrAnnounce.setTextColor(Color.parseColor("#990000"));
        }

        TextView userAnsShow = (TextView) rootView.findViewById(R.id.userAns);
        TextView corrAnsShow = (TextView) rootView.findViewById(R.id.corrAns);

        userAnsShow.setText("You answered: " + questions.get(useAns).getQuestion());
        corrAnsShow.setText("Correct answer: " + questions.get(correctAns).getQuestion());
        TextView scoreReport = (TextView) rootView.findViewById(R.id.scoreReport);
        scoreReport.setText("Answered " + score+" correctly out of " + answered);
        Button button = (Button) rootView.findViewById(R.id.next);
        if(qNum <= topics.get(quizNumber).getQuestions().size()){
            button.setText("Next Question");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchingIntent.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("question", qNum);
                    launchingIntent.putExtra("score", score);
                    launchingIntent.putExtra("answered", answered);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

                            .replace(R.id.container, new Question())
                            .commit();
                }
            });
        } else {
            button.setText("Finish");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent next = new Intent(getActivity(), MainActivity.class);
                    startActivity(next);
                    getActivity().finish();
                }
            });
        }


        return rootView;
    }



}
