package edu.washington.mikhail3.quizdroid;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
import java.io.Serializable;
public class Quiz implements Serializable {
    private String question;
    private String ans1;
    private String ans2;
    private String ans3;
    private String ans4;
    private int correct;

    public Quiz() {
    }
    public String getQuestion() {
        return question;
    }
    public String getAns1() {
        return ans1;
    }
    public String getAns2() {
        return ans2;
    }
    public String getAns3() {
        return ans3;
    }
    public String getAns4() {
        return ans4;
    }
    public String getAns(int num){
        if (num == 1){return ans1;}
        else if (num == 2){return ans2;}
        else if (num == 3){return ans3;}
        else if (num == 4){return ans4;}
        else return null;
    }
    public int getCorrect() {
        return correct;
    }
    public void setQuestion(String q) {
        this.question = q;
    }
    public void setAns1(String ans) {
        this.ans1 = ans;
    }
    public void setAns2(String ans) {
        this.ans2 = ans;
    }
    public void setAns3(String ans) {
        this.ans3 = ans;
    }
    public void setAns4(String ans) {
        this.ans4 = ans;
    }
    public void setCorrect(int corr) {
        this.correct = corr;
    }
}

