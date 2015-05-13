package edu.washington.mikhail3.quizdroid;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
import java.io.Serializable;
import java.util.ArrayList;
public class Topic implements Serializable{
    private String title;
    private String description;
    private ArrayList<Quiz> questions;

    public Topic() {
        questions = new ArrayList<Quiz>();
    }
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return description;
    }
    public ArrayList<Quiz> getQuestions() {
        return questions;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setQuestions(ArrayList<Quiz> questions) {
        this.questions = questions;
    }
    public void setDesc(String desc) {
        this.description = desc;
    }
}
