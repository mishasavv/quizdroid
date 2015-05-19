package edu.washington.mikhail3.quizdroid;


import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.preference.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Filter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<Topic> topics;
    private DownloadManager manager;
    QuizApp quizApp;
    BroadcastReceiver ar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Toast.makeText(MainActivity.this, preferences.getString("down_url", ""), Toast.LENGTH_LONG).show();
        }
    };
    private final BroadcastReceiver r = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (downloadID != 0) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = manager.query(query);
                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (status) {
                            case DownloadManager.STATUS_RUNNING:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_PAUSED:
                            break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                ParcelFileDescriptor file;
                                try {
                                    QuizApp quizApp = (QuizApp) getApplication();
                                    file = manager.openDownloadedFile(downloadID);
                                    FileInputStream in = new FileInputStream(file.getFileDescriptor());
                                    String json = SingleApp.loadJSON(in);
                                    SingleApp.write(json, quizApp.getApplicationContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                Log.i("MainActivity", "Fail");
                                System.exit(0);
                                break;
                        }
                    }
                }
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_downloads, false);
        quizApp = (QuizApp) getApplication();
        topics = quizApp.getQuiz();
        final ArrayList<String> quizzes = new ArrayList<String>();
        int length = topics.size();
        for (int i = 0; i < length; i++) {
            Topic topic = topics.get(i);
            quizzes.add(topic.getTitle());
        }
        ListView quizList = (ListView) findViewById(R.id.quizList);
        quizList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_activated_1, quizzes));
        quizList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent next = new Intent(MainActivity.this, QuizActivity.class);
                next.putExtra("quiz", position);
                next.putExtra("quizData", topics);
                startActivity(next);
                finish();
            }
        });
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        IntentFilter f = new IntentFilter();
        f.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(r, f);
        alarmStartStop(this, true);
    }

    public void alarmStartStop(Context context, boolean turnedOn) {
        Intent alarmReceiverIntent = new Intent();
        registerReceiver(ar, new IntentFilter("edu.washington.mikhail3.quizdroid"));
        alarmReceiverIntent.setAction("edu.washington.mikhail3.quizdroid");
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (turnedOn) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(preferences == null){
                Log.i("1","1");
            }
            int interval = (Integer.parseInt(preferences.getString("down_freq", null)) * 60 * 1000);
            //int interval = 10000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
        } else {
            manager.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this, prefActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unregisterReceiver(ar);
        unregisterReceiver(r);
    }
}


