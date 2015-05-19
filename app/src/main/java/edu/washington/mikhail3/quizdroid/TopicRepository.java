package edu.washington.mikhail3.quizdroid;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misha Savvateev on 5/11/2015.
 */
public interface TopicRepository {
    public List<Topic> getQuiz();
}
