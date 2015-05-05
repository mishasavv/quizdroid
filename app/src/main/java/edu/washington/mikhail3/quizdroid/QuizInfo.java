package edu.washington.mikhail3.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class QuizInfo extends android.support.v4.app.Fragment {
    private int questionNumber;
    private int[] questions = {3,3,3};
    String[] quizzes = {"math", "physics", "superhero"};
    public QuizInfo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz_info, container, false);
        final Intent launchingIntent = getActivity().getIntent();
        final int quizNumber = launchingIntent.getIntExtra("quiz", -1);
        int q = launchingIntent.getIntExtra("question", -1);
        if(q != -1){
            questionNumber = q;
        } else {
            questionNumber = 1;
        }
        final String quizName = launchingIntent.getStringExtra("quizName");
        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView qNum = (TextView) rootView.findViewById(R.id.qSetNum);
        title.setText(quizName);
        qNum.setText("This quiz has "+questions[quizNumber]+" questions. Enjoy!");

        String quizId = quizzes[quizNumber]+"_quiz_info";
        //int id = getResources().getIdentifier(quizId, "string",getActivity().getPackageName());
        //String descr = getResources().getString(id);
        //Log.i(quizId, id + "");
        //TextView description = (TextView) rootView.findViewById(R.id.quizDescr);
        //description.setText(descr);


        String id = quizzes[quizNumber]+"_quiz_info";
        TextView description = (TextView) rootView.findViewById(R.id.quizDescr);
        description.setText(getResources().getString(getResources().getIdentifier(id, "string", getActivity().getPackageName())));

        Button button= (Button) rootView.findViewById(R.id.startQuiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent next = new Intent(QuizInfoActivity.this, question.class);
                launchingIntent.putExtra("quiz", quizNumber);
                launchingIntent.putExtra("quizName", quizName);
                launchingIntent.putExtra("question", questionNumber);
                launchingIntent.putExtra("total", questions[quizNumber]);
                launchingIntent.putExtra("answeredNum", 0);
                launchingIntent.putExtra("score", 0);
                launchingIntent.putExtra("quizNameID", quizzes[quizNumber]);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.container, new Question())
                        .commit();
            }
        });
        return rootView;
    }
}