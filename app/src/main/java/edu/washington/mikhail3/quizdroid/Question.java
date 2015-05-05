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


public class Question extends android.support.v4.app.Fragment {
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


    public Question() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        final Intent launchingIntent = getActivity().getIntent();
        quizNumber = launchingIntent.getIntExtra("quiz", -1);
        quizName = launchingIntent.getStringExtra("quizName");
        quizId = launchingIntent.getStringExtra("quizNameID");
        totalQ = launchingIntent.getIntExtra("total", 0);
        qNum = launchingIntent.getIntExtra("question", 0);
        TextView title = (TextView) rootView.findViewById(R.id.quizTitle);
        title.setText(quizName);
        answered = launchingIntent.getIntExtra("answered", 0);
        answered++;
        score = launchingIntent.getIntExtra("score", 0);
        TextView text = (TextView) rootView.findViewById(R.id.questionText);
        RadioButton ans1 = (RadioButton) rootView.findViewById(R.id.ans1);
        RadioButton ans2 = (RadioButton) rootView.findViewById(R.id.ans2);
        RadioButton ans3 = (RadioButton) rootView.findViewById(R.id.ans3);
        RadioButton ans4 = (RadioButton) rootView.findViewById(R.id.ans4);
        String qId = quizId+"_q_"+qNum;
        String a1Id = quizId+qNum+"_a_1";
        String a2Id = quizId+qNum+"_a_2";
        String a3Id = quizId+qNum+"_a_3";
        String a4Id = quizId+qNum+"_a_4";
        text.setText(getResources().getString(getResources().getIdentifier(qId, "string", getActivity().getPackageName())));
        ans1.setText(getResources().getString(getResources().getIdentifier(a1Id, "string", getActivity().getPackageName())));
        ans2.setText(getResources().getString(getResources().getIdentifier(a2Id, "string", getActivity().getPackageName())));
        ans3.setText(getResources().getString(getResources().getIdentifier(a3Id, "string", getActivity().getPackageName())));
        ans4.setText(getResources().getString(getResources().getIdentifier(a4Id, "string", getActivity().getPackageName())));
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
                    //next.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("quizName", quizName);
                    launchingIntent.putExtra("score", score);
                    launchingIntent.putExtra("question", qNum);
                    launchingIntent.putExtra("quiz", quizNumber);
                    launchingIntent.putExtra("total", totalQ);
                    launchingIntent.putExtra("quizNameID", quizId);
                    launchingIntent.putExtra("ansNumber", ansNumber);
                    launchingIntent.putExtra("correctNum", correctNum);
                    launchingIntent.putExtra("correct", correct);
                    launchingIntent.putExtra("answered", answered);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.container, new ScorePage())
                            .commit();
                }
            }
        });
        return rootView;
    }


}
