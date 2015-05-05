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


public class ScorePage extends android.support.v4.app.Fragment {
    int totalQ;
    int score;
    int answered;
    int qNum;
    String quizName;
    int quizNumber;
    String quizID;
    int correctAns;
    int useAns;

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
        quizName = launchingIntent.getStringExtra("quizName");
        quizID = launchingIntent.getStringExtra("quizNameID");
        totalQ = launchingIntent.getIntExtra("total", 0);
        qNum = launchingIntent.getIntExtra("question", 0);
        answered = launchingIntent.getIntExtra("answered", 0);
        score = launchingIntent.getIntExtra("score", 0);
        correctAns = launchingIntent.getIntExtra("correctNum", 0) + 1;
        useAns = launchingIntent.getIntExtra("ansNumber", 0) + 1;
        boolean correct = launchingIntent.getBooleanExtra("correct", false);
        TextView title = (TextView) rootView.findViewById(R.id.quizTitle1);
        title.setText(quizName);
        TextView corrAnnounce = (TextView) rootView.findViewById(R.id.correct);
        if(correct){
            corrAnnounce.setText("CORRECT!");
            corrAnnounce.setTextColor(Color.parseColor("#093600"));
        } else {
            corrAnnounce.setText("INCORRECT");
            corrAnnounce.setTextColor(Color.parseColor("#990000"));
        }
        String useAnsCode = quizID + "" + (qNum-1) + "_a_"+(useAns);
        String corrAnsCode = quizID + "" + (qNum-1) + "_a_"+(correctAns);


        TextView userAnsShow = (TextView) rootView.findViewById(R.id.userAns);
        TextView corrAnsShow = (TextView) rootView.findViewById(R.id.corrAns);

        userAnsShow.setText("You answered: " + getResources().getString(getResources().getIdentifier(useAnsCode, "string", getActivity().getPackageName())));
        corrAnsShow.setText("Correct answer: " + getResources().getString(getResources().getIdentifier(corrAnsCode, "string", getActivity().getPackageName())));
        //userAnsShow.setText("user="+useAnsCode);
        //corrAnsShow.setText("corr="+corrAnsCode);
        TextView scoreReport = (TextView) rootView.findViewById(R.id.scoreReport);
        scoreReport.setText("Answered " + score+" correctly out of " + answered);
        Button button = (Button) rootView.findViewById(R.id.next);
        if(qNum <= totalQ){
            button.setText("Next Question");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchingIntent.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("quizName", quizName);
                    launchingIntent.putExtra("question", qNum);
                    launchingIntent.putExtra("total", totalQ);
                    launchingIntent.putExtra("score", score);
                    launchingIntent.putExtra("quizNameID", quizID);
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
